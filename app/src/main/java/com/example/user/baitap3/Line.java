package com.example.user.baitap3;

/**
 * Created by User on 3/28/2018.
 */

public class Line {
        private int startx;
        private int starty;
        private int stopx;
        private int stopy;
        // phương thức khởi tạo được gọi khi new đôí tượng=> public
        public Line(int startx, int starty, int stopx, int stopy) {
            this.startx = startx;
            this.starty = starty;
            this.stopx = stopx;
            this.stopy = stopy;
        }

        public int getStartx() {
            return startx;
        }

        public void setStartx(int startx) {
            this.startx = startx;
        }

        public int getStarty() {
            return starty;
        }

        public void setStarty(int starty) {
            this.starty = starty;
        }

        public int getStopx() {
            return stopx;
        }

        public void setStopx(int stopx) {
            this.stopx = stopx;
        }

        public int getStopy() {
            return stopy;
        }

        public void setStopy(int stopy) {
            this.stopy = stopy;
        }
    }


