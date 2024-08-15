-- Table for users
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL 
);

-- Table for tracking budgets
CREATE TABLE budget (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, 
    name VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table for tracking expenses
CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,  
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES expense_category(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table for expense categories
CREATE TABLE expense_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, 
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table for financial goals
CREATE TABLE financial_goal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, 
    description VARCHAR(255) NOT NULL,
    target_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table for tracking income
CREATE TABLE income (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, 
    source VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
