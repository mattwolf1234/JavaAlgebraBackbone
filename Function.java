/*
 * Copyright 2018 Matthew Beck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Function extends Element {
    private String functionName;

    private boolean trigonometry;
    private int trig;
    private boolean trigMode = false;// false is degrees, true is radians

    public static final int TRIG_SIN = 0;
    public static final int TRIG_COS = 1;
    public static final int TRIG_TAN = 2;
    public static final int TRIG_CSC = 3;
    public static final int TRIG_SEC = 4;
    public static final int TRIG_COT = 5;
    public static final int TRIG_ASIN = 6; // this is the inverse of sin
    public static final int TRIG_ACOS = 7;
    public static final int TRIG_ATAN = 8;
    public static final int TRIG_ACSC = 9;
    public static final int TRIG_ASEC = 10;
    public static final int TRIG_ACOT = 11;

    public Function() {
        functionName = "f"; // default f(x)
        trigonometry = false;
    }

    public Function(String functionName) {
        this.functionName = functionName;
        trigonometry = false;
    }

    /*
    Trigonometry is defaulted to true with this constructor
     */
    public Function(int trig) {
        this.trig = trig;
        trigonometry = true;
    }
    public Function(int trig, boolean trigMode) {
        this.trig = trig;
        trigonometry = true;
        this.trigMode = trigMode;
    }

    public int getTrig() {
        return trig;
    }

    public void setTrig(int trig) {
        this.trig = trig;
    }

    public String getFunctionName() {
        return trigonometry ? getTrigName() : functionName;
    }

    public String getTrigName() {
        String returnString;
        switch (trig) {
            case TRIG_SIN:
                returnString = "sin";
                break;
            case TRIG_COS:
                returnString = "cos";
                break;
            case TRIG_TAN:
                returnString = "tan";
                break;
            case TRIG_CSC:
                returnString = "csc";
                break;
            case TRIG_SEC:
                returnString = "sec";
                break;
            case TRIG_COT:
                returnString = "cot";
                break;
            case TRIG_ACOT:
                returnString = "arccot";
                break;
            case TRIG_ASIN:
                returnString = "arcsin";
                break;
            case TRIG_ACOS:
                returnString = "arccos";
                break;
            case TRIG_ATAN:
                returnString = "arctan";
                break;
            case TRIG_ACSC:
                returnString = "arccsc";
                break;
            case TRIG_ASEC:
                returnString = "arcsec";
                break;
            default:
                returnString = "";
                break;
        }
        return returnString;
    }

    @Override
    public String toString() {
        if (trigonometry) {
            return getTrigName();
        }
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public boolean isTrigonometry() {
        return trigonometry;
    }

    /*
    Default input is degrees
     */
    public <T> Digit inputTrig(Node<T> list) {
        Digit digit = null;

        if (list.getChildren().size() == 1 && list.getChildren().get(0).getData() instanceof Digit) {
            digit = new Digit();
            double num;
            if (trigMode){// radians
                num = ((Digit) list.getChildren().get(0).getData()).getDigit();
            }else{// degrees
                num = Math.toRadians(((Digit) list.getChildren().get(0).getData()).getDigit());
            }
            switch (trig) {
                case TRIG_SIN:
                    digit.setDigit(Math.sin(num));
                    break;
                case TRIG_COS:
                    digit.setDigit(Math.cos(num));
                    break;
                case TRIG_TAN:
                    digit.setDigit(Math.tan(num));
                    break;
                case TRIG_COT:
                    digit.setDigit(1 / Math.tan(num));
                    break;
                case TRIG_CSC:
                    digit.setDigit(1 / Math.sin(num));
                    break;
                case TRIG_SEC:
                    digit.setDigit(1 / Math.cos(num));
                    break;
                case TRIG_ASIN:
                    digit.setDigit(Math.toDegrees(Math.asin(num)));
                    break;
                case TRIG_ACOS:
                    digit.setDigit(Math.toDegrees(Math.acos(num)));
                    break;
                case TRIG_ATAN:
                    digit.setDigit(Math.toDegrees(Math.atan(num)));
                    break;
                case TRIG_ACSC:
                    digit.setDigit(Math.toDegrees(Math.asin(1/num)));
                    break;
                case TRIG_ASEC:
                    digit.setDigit(Math.toDegrees(Math.acos(1/num)));
                    break;
                case TRIG_ACOT:
                    digit.setDigit(Math.toDegrees(Math.atan(1/num)));
                    break;
            }
        }
        System.out.println(digit);

        return digit;
    }

    public boolean isTrigMode() {
        return trigMode;
    }

    public void setTrigMode(boolean trigMode) {
        this.trigMode = trigMode;
    }
}
