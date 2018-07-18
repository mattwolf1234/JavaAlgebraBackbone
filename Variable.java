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

public class Variable {
    char var;
    public Variable(){
        var = 'x';
    }
    public Variable(char var){
        this.var = var;
    }

    public char getVar() {
        return var;
    }

    public void setVar(char var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return String.valueOf(var);
    }
}
