package com.yi.business.geo;


public class Coordinates {
    private Coordinate coordinateA = new Coordinate();
    private Coordinate coordinateB = new Coordinate();
    private Coordinate coordinateC = new Coordinate();
    private Coordinate coordinateD = new Coordinate();

    public Coordinate getCoordinateA() {
        return coordinateA;
    }

    public void setCoordinateA(Coordinate coordinateA) {
        this.coordinateA = coordinateA;
    }

    public Coordinate getCoordinateB() {
        return coordinateB;
    }

    public void setCoordinateB(Coordinate coordinateB) {
        this.coordinateB = coordinateB;
    }

    public Coordinate getCoordinateC() {
        return coordinateC;
    }

    public void setCoordinateC(Coordinate coordinateC) {
        this.coordinateC = coordinateC;
    }

    public Coordinate getCoordinateD() {
        return coordinateD;
    }

    public void setCoordinateD(Coordinate coordinateD) {
        this.coordinateD = coordinateD;
    }

    public Coordinates(String coordinateStrA, String coordinateStrB, String coordinateStrC, String coordinateStrD) {
        super();
        this.coordinateA = new Coordinate(coordinateStrA);
        this.coordinateB = new Coordinate(coordinateStrB);
        this.coordinateC = new Coordinate(coordinateStrC);
        this.coordinateD = new Coordinate(coordinateStrD);
    }

    public Coordinates(Coordinate coordinateA, Coordinate coordinateB, Coordinate coordinateC, Coordinate coordinateD) {
        super();
        this.coordinateA = coordinateA;
        this.coordinateB = coordinateB;
        this.coordinateC = coordinateC;
        this.coordinateD = coordinateD;
    }

    public Coordinates() {
        super();
    }

    public class Coordinate {
        private String longitude = "";
        private String latitude = "";

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public Coordinate(String longitude, String latitude) {
            super();
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public Coordinate(String coordinateStr) {
            super();
            if (coordinateStr == null || coordinateStr.length() <= 0) {
                this.longitude = "";
                this.latitude = "";
            } else if (coordinateStr.split(",").length == 2) {
                this.longitude = coordinateStr.split(",")[0];
                this.latitude = coordinateStr.split(",")[1];
            }

        }

        public Coordinate() {
            super();
        }
    }

}
