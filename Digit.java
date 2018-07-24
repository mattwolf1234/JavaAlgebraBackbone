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

// this class holds a single number
public class Digit extends Element{
    private double digit;

    public Digit(){
        digit = 0;
    }
    public Digit(double digit){
        this.digit = digit;
    }

    public double getDigit() {
        return digit;
    }

    public void setDigit(double digit) {
        this.digit = digit;
    }

    public void addDigit(Digit num){
        this.digit += num.getDigit();
    }

    public void mulDigit(Digit num){
        this.digit *= num.getDigit();
    }
    public void divDigit(Digit num){
        this.digit /= num.getDigit();
    }
    public void subDigit(Digit num){
        this.digit -= num.getDigit();
    }

    @Override
    public String toString() {
        return Double.toString(digit);
    }

    public String getFractionText(){// this returns the number as a fraction if it is a decimal
        int[] temp = CommonOperations.convertDecimalToFraction(digit);
        return temp[0] + "/" + temp[1];
    }
}
