package com.delta.Builder;

public class MacBookBuilder extends Builder {

    private MacBook macBook = new MacBook();
    @Override
    public void buildBoard(String board) {
        macBook.setmBoard(board);
    }

    @Override
    public void buildDisplay(String display) {
        macBook.setmDisplay(display);
    }

    @Override
    public void buildOs(String os) {
        macBook.setmOs(os);
    }

    @Override
    public MacBook getMacBook() {
        return macBook;
    }
}
