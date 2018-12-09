package site.zido.elise.select.api;

public interface Code extends Matchable, SelectResult, MatchResult {
    Code saveAsString();

    Code saveAsNumber();
}
