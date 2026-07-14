package com.ataraxia.atxcore.mechanic.condition;

public record ConditionResult(boolean passed, String reason) {
    public static ConditionResult pass() {
        return new ConditionResult(true, "");
    }

    public static ConditionResult fail(String reason) {
        return new ConditionResult(false, reason);
    }
}
