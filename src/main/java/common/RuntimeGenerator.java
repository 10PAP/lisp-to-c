package common;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class RuntimeGenerator {
    public static void generateHeader() {
        try (PrintWriter out = new PrintWriter("out/runtime.h")) {
            out.write("""
                    #ifndef RUNTIME_LISP_HEADER_123456789
                    #define RUNTIME_LISP_HEADER_123456789
                    
                    #include <stdio.h>
                    #include <math.h>
                    #include <stdlib.h>

                    struct Int ;
                    struct Boolean ;
                    struct String ;
                    
                    union Value ;

                    enum Tag { VOID, INT, BOOLEAN, STRING, CLOSURE, CELL, ENV } ;

                    struct Int {
                      enum Tag t ;
                      int value ;
                    } ;

                    struct Boolean {
                      enum Tag t ;
                      unsigned int value ;
                    } ;
                    
                    struct String {
                      enum Tag t ;
                      char * value ;
                    } ;

                    struct Cell {
                      enum Tag t ;
                      union Value* addr ;
                    } ;

                    union Value {
                      enum Tag t ;
                      struct Int z ;
                      struct Boolean b ;
                      struct Cell cell ;
                      struct String s ;
                    } ;

                    typedef union Value Value ;

                    static Value MakeInt(int n) {
                      Value v ;
                      v.z.t = INT ;
                      v.z.value = n ;
                      return v ;
                    }

                    static Value MakeBoolean(unsigned int b) {
                      Value v ;
                      v.b.t = BOOLEAN ;
                      v.b.value = b ;
                      return v ;
                    }
                    
                    static Value MakeString(char * str) {
                      Value v ;
                      v.s.t = STRING;
                      v.s.value = str;
                      return v ;
                    }

                    static Value NewCell(Value initialValue) {
                      Value v ;
                      v.cell.t = CELL ;
                      v.cell.addr = malloc(sizeof(Value)) ;
                      *v.cell.addr = initialValue ;
                      return v ;
                    }
                    
                    // RUNTIME FUNCTIONS DECLARATION
                    Value lisp_add(Value a, Value b);
                    Value lisp_sub(Value a, Value b);
                    Value lisp_mul(Value a, Value b);
                    Value lisp_div(Value a, Value b);
                    Value lisp_mod(Value a, Value b);
                    Value lisp_inc(Value a);
                    Value lisp_dec(Value a);
                    void lisp_print(Value a);

                    #endif""");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void generateCFile() {
        try (PrintWriter out = new PrintWriter("out/runtime.c")) {
            out.write("""
                    #include "runtime.h"
                                        
                    // addition operation
                    Value lisp_add(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value + b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // subtraction operation
                    Value lisp_sub(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value - b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // multiplication operation
                    Value lisp_mul(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value * b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // division operation
                    Value lisp_div(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value / b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // modulus operation
                    Value lisp_mod(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value % b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // increment a by 10
                    Value lisp_inc(Value a) {
                    	if (a.t == INT) {
                    		a.z.value++;
                    	}
                    	return a;
                    }
                     \s
                    // decrement b by 20
                    Value lisp_dec(Value a) {
                    	if (a.t == INT) {
                    		a.z.value--;
                    	}
                    	return a;
                    }
                    
                    void lisp_print(Value a) {
                        if (a.t == INT) {
                            printf("%d ", a.z.value);
                        }
                    }
                    """);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
