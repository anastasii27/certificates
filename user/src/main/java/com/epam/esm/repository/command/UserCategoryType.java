package com.epam.esm.repository.command;

public enum UserCategoryType {
    ORDER_MAX_SUM("user_orders_cost_max", new UserOrdersSumMaxCostCategory());

    private UserCategoryCommand command;
    private String category;

    UserCategoryType(String category, UserCategoryCommand command) {
        this.category = category;
        this.command = command;
    }

    public static UserCategoryCommand getCommand(String category) {
        for(UserCategoryType type: UserCategoryType.values()){
            if(type.category.equals(category)){
                return type.command;
            }
        }
        throw new IllegalArgumentException("exception.illegal_category");
    }
}
