# Bonus task: Selenium + Junit

------------------------------------------------

## How would I use Selenium and Junit?

Selenium is used to automate UI testing, while JUnit is the test runner and assertion framework.

After analysing the task I created the following scenario for the Delete task functionality:
- Firstly I retrieve the number of rows in the task table. This represents the existing tasks.
- Then I locate the 'Delete' button with xPath selector. Before clicking it, I verify that it is visible on the page.
- After clicking the Delete button, I use an explicit wait to ensure the DOM updates properly. The test waits until the number of task rows becomes smaller than the original count.
- Then I verify that the rows has decreased by exactly one.
- Finally, to ensure the correct task was removed, I collect all task titles from the table and assert that the deleted task’s title is no longer present.