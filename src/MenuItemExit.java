
//Опция меню для выхода из программы.
class MenuItemExit implements MenuItem {
    public String description() {
        return "Terminating program.\n" +
                "See you again, sir!";
    }

    public void select() {
        System.out.println(description());
        System.exit(0);
    }

}
