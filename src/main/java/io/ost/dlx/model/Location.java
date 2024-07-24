package io.ost.dlx.model;

import java.util.List;

/**
 *
 * @author Joost
 */
public class Location {
    
        public Coordinate coordinate;
        public Level level;
        public Room room;
        public Building building;
        public Drawing drawing;
        public BimObject bimObject;
        public List<LocationImage> locationImages;
        public List<ZoneLayer> zones;

        public class Coordinate {

            public Xyz xyz;
            public Gps gps;

            public class Xyz {

                public String x;
                public String y;
                public String z;
            }

            public class Gps {

                public String lat;
                public String lng;
            }
        }

        public class Level {

            public String name;
        }

        public class Room {

            public String name;
        }

        public class Building {

            public String name;
        }

        public class Drawing {

            public String name;
        }

        public class BimObject {

            public String name;
            public String categoryName;
        }

        public class LocationImage {

            public String name;
            public String fileDownload;
        }

        public class ZoneLayer {
            
            public Zone zone;
            public Layer layer;

            public class Zone {

                public String name;
            }

            public class Layer {

                public String name;
            }
        }
}
