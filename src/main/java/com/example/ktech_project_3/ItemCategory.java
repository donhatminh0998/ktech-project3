package com.example.ktech_project_3;

public enum ItemCategory {
    FASHION,
    BEAUTY,
    HOUSEHOLD,
    KID_TOYS,
    SPORTS;

    public String getCategoryName() {
        return this.name();
    }

    public static Object getCategoryFromName(String s) {
        try {
            return ItemCategory.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
