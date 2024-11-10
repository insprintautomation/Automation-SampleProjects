@order @regression @smoke
Feature: Product Order

  As a customer, I should be able to browse products, add products to cart and place order successfully.

  Background:
    Given I am in login page

  @smoke @positive
  Scenario Outline: Successfully place order for product(s) - <products>
    Given I login as a <accountType> user
    When I add '<products>' products to cart
    And I proceed to checkout
    And I enter customer information
    And I review & submit the order
    Then I should see order confirmation
    @good
    Examples:
      | accountType | products                                       |
      | standard    | sauce-labs-backpack                            |
      | visual      | sauce-labs-bike-light, sauce-labs-bolt-t-shirt |
