package lotto.domain;

import java.util.Objects;

public class WinningLotto {
    private final Lotto winningLotto;
    private final Number bonusBall;

    public WinningLotto(Lotto winningLotto, Number bonusBall) {
        checkWinningLotto(winningLotto,bonusBall);
        this.winningLotto = winningLotto;
        this.bonusBall = bonusBall;
    }

    private void checkWinningLotto(Lotto winningLotto, Number bonusBall) {
        if(winningLotto.isContain(bonusBall)){
            throw new IllegalArgumentException("보너스볼은 당첨 번호에 없어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WinningLotto that = (WinningLotto) o;
        return Objects.equals(winningLotto, that.winningLotto) &&
                Objects.equals(bonusBall, that.bonusBall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(winningLotto, bonusBall);
    }

    public Rank matchLotto(Lotto lotto) {
        int countOfMatch = winningLotto.matchNumber(lotto);
        boolean matchBonus = lotto.isContain(bonusBall);
        return Rank.valueOf(countOfMatch,matchBonus);
    }
}
