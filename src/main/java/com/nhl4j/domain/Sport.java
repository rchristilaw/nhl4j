package com.nhl4j.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.nhl4j.domain.ApiSource.*;

@Getter
@RequiredArgsConstructor
public enum Sport {
    BASEBALL(ESPN_MLB),
    BASKETBALL(null),
    FOOTBALL(ESPN_NFL),
    HOCKEY(ESPN_NHL);

    private final ApiSource apiSource;
}
