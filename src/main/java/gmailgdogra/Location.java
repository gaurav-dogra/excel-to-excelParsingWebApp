package gmailgdogra;

public enum Location {
    MAIN_GATE {
        @Override
        public String toString() {
            return "Main Gate";
        }
    },
    VISITORS_RECEPTION {
        @Override
        public String toString() {
            return "Visitors Reception";
        }
    },
    EP_WEIGHBRIDGE {
        @Override
        public String toString() {
            return "EP Weighbridge";
        }
    },
    TL_PLAISTOW {
        @Override
        public String toString() {
            return "Plaistow";
        }
    }
}
