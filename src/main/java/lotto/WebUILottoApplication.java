package lotto;

import lotto.dao.LottosDao;
import lotto.dao.RoundDao;
import lotto.dao.WinningLottoDao;
import lotto.db.DatabaseConnection;
import lotto.domain.*;
import lotto.service.LottoResultService;
import lotto.service.LottoService;
import lotto.service.WinningLottoService;
import lotto.utils.ResultMessage;
import lotto.utils.ViewUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class WebUILottoApplication {

    public static void main(String[] args) {
        externalStaticFileLocation("src/main/resources/templates");
        staticFiles.location("/static");

        get("/", (req, res) -> {
                    Connection conn = new DatabaseConnection().getConnection();
                    RoundDao roundDao = new RoundDao(conn);
                    Map<String, Object> model = new HashMap<>();
                    List<Integer> rounds = roundDao.findAllRound();
                    model.put("present", roundDao.findLatestRound());
                    model.put("message", req.queryParams("message"));
                    return ViewUtils.render(model, "home.html");
                }
        );

        get("/result",(req,res)->{
            Connection conn = new DatabaseConnection().getConnection();
            LottosDao lottosDao = new LottosDao(conn);
            WinningLottoDao winningLottoDao = new WinningLottoDao(conn);
            Map<String,Object> model = new HashMap<>();

            int round = Integer.parseInt(req.queryParams("round"));
            Lottos lottos = lottosDao.findLottoByRound(round);
            WinningLotto winningLotto = winningLottoDao.findWinningLottoByRound(round);
            LottoResult lottoResult = LottoResult.generateLottoResult(lottos,winningLotto);
            model.put("round",round);
//            model.put("yield", lottoResult.findYield(price.getPrice()));
//            model.put("userLottoResult", ResultMessage.getResult(lottoResult, getRanks()));
            return ViewUtils.render(model,"result");
        });

        post("/make/lotto", LottoService.makeSelfLottoPage);

        post("/lottos", LottoService.makeUserLottosPage);

        post("/make/winning", WinningLottoService.makeWinningLottoPage);

        post("/result", LottoResultService.makeLottoResultPage);

        exception(Exception.class, (exception, req, res) -> {
            String message = null;
            try {
                message = encodeUTF8(exception.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res.redirect("/?message=" + message);

        });

    }

    private static String encodeUTF8(final String message) throws UnsupportedEncodingException {
        return URLEncoder.encode(message, "UTF-8");
    }

}