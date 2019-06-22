package lotto.dao;

import lotto.db.DatabaseConnection;
import lotto.domain.Lotto;
import lotto.domain.Lottos;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class LottosDaoTest {

    private Connection conn;
    private LottosDao lottosDao;
    private List<Lotto> lottos;

    @BeforeEach
    void setUp() throws SQLException{
        conn = new DatabaseConnection().getConnection();
        conn.setAutoCommit(false);
        lottosDao = new LottosDao(conn);
        lottos = Arrays.asList(new Lotto(Arrays.asList(1,2,3,4,5,6)));
    }

    @Test
    void Lottos_추가() throws SQLException {
        lottosDao.addLottos(100,new Lottos(lottos));
    }

    @Test
    void Lotto_조회() throws SQLException {
        lottosDao.addLottos(100,new Lottos(lottos));
        assertThat(lottosDao.findLottoByRound(100)).isEqualTo(new Lottos(lottos));
    }

    @AfterEach
    void tearDown() throws SQLException{
        conn.rollback();
    }

}