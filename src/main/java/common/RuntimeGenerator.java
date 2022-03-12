package common;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class RuntimeGenerator {

    public static void generateMakefile() {
        try (PrintWriter out = new PrintWriter("out/Makefile")) {
            out.write("""
                    all:
                    \tgcc main.c runtime.c
                        
                    """);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void generateHeader() {
        try (PrintWriter out = new PrintWriter("out/runtime.h")) {
            out.write("""
                    #ifndef RUNTIME_LISP_HEADER_123456789
                    #define RUNTIME_LISP_HEADER_123456789
                    
                    #include <stdio.h>
                    #include <math.h>
                    #include <stdlib.h>
                    #include <stdarg.h>

                    struct Int ;
                    struct Boolean ;
                    struct String ;
                    
                    union Value ;

                    enum Tag { VOID, INT, BOOLEAN, STRING, CLOSURE, CELL, ENV, LIST } ;

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
                    
                    struct ValueList {
                      enum Tag t ;
                      union Value* values ;
                      int cnt ;
                    } ;

                    union Value {
                      enum Tag t ;
                      struct Int z ;
                      struct Boolean b ;
                      struct Cell cell ;
                      struct String s ;
                      struct ValueList list;
                    } ;

                    typedef union Value Value ;
                    
                    // RUNTIME FUNCTIONS DECLARATION
                    Value MakeInt(int n);
                    Value MakeBoolean(unsigned int b);
                    Value MakeString(char * str);
                    Value NewCell(Value initialValue);
                    
                    
                    Value lisp_add(Value a, Value b);
                    Value lisp_sub(Value a, Value b);
                    Value lisp_mul(Value a, Value b);
                    Value lisp_div(Value a, Value b);
                    Value lisp_mod(Value a, Value b);
                    Value lisp_inc(Value a);
                    Value lisp_dec(Value a);
                    
                    void lisp_print(Value a);
                    
                    Value lisp_gt(Value a, Value b);
                    Value lisp_lt(Value a, Value b);
                    Value lisp_eq(Value a, Value b);
                    Value lisp_and(Value a, Value b);
                    Value lisp_or(Value a, Value b);
                    Value lisp_not(Value a);
                    
                    Value lisp_list(int cnt, ...);
                    

                    #endif""");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void generateCFile() {
        try (PrintWriter out = new PrintWriter("out/runtime.c")) {
            out.write("""
                    #include "runtime.h"
                            
                    Value MakeInt(int n) {
                      Value v ;
                      v.z.t = INT ;
                      v.z.value = n ;
                      return v ;
                    }

                    Value MakeBoolean(unsigned int b) {
                      Value v ;
                      v.b.t = BOOLEAN ;
                      v.b.value = b ;
                      return v ;
                    }
                    
                    Value MakeString(char * str) {
                      Value v ;
                      v.s.t = STRING;
                      v.s.value = str;
                      return v ;
                    }

                    Value NewCell(Value initialValue) {
                      Value v ;
                      v.cell.t = CELL ;
                      v.cell.addr = malloc(sizeof(Value)) ;
                      *v.cell.addr = initialValue ;
                      return v ;
                    }
                    
                    Value lisp_list(int cnt, ...) {
                        va_list ap ;
                        va_start(ap, cnt) ;
                        Value v ;
                        v.list.t = LIST ;
                        v.list.cnt = cnt ;
                        v.list.values = (union Value*)malloc(sizeof(Value) * cnt) ;
                        for(int j = 0 ; j < cnt ; j++){
                          v.list.values[j] = va_arg(ap, Value) ;
                        }
                        va_end(ap) ;
                        return v ;
                    }
                    
                    /*
                     * ARITHMETIC OPERATIONS
                     */
                                 
                    // addition operation
                    inline Value lisp_add(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value + b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // subtraction operation
                    inline Value lisp_sub(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value - b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // multiplication operation
                    inline Value lisp_mul(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value * b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // division operation
                    inline Value lisp_div(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value / b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // modulus operation
                    inline Value lisp_mod(Value a, Value b) {
                    	if (a.t == INT && b.t == INT) {
                    		a.z.value = a.z.value % b.z.value ;
                    	}
                    	return a;
                    }
                     \s
                    // increment a by 10
                    inline Value lisp_inc(Value a) {
                    	if (a.t == INT) {
                    		a.z.value++;
                    	}
                    	return a;
                    }
                     \s
                    // decrement b by 20
                    inline Value lisp_dec(Value a) {
                    	if (a.t == INT) {
                    		a.z.value--;
                    	}
                    	return a;
                    }
                    
                    /*
                     *     I/O
                     */
                    
                    void lisp_print(Value a) {
                        if (a.t == INT) {
                            printf("%d ", a.z.value);
                        }
                        if (a.t == BOOLEAN) {
                            if (a.b.value == 1) {
                                printf("true ");
                            } else {
                                printf("false ");
                            }
                        }
                        if (a.t == LIST) {
                            printf("(");
                            for(int j = 0 ; j < a.list.cnt ; j++){
                                lisp_print(a.list.values[j]); 
                            } 
                            printf(") ");
                        }
                    }
                    
                    /*
                     * BOOLEAN OPERATIONS
                     */
                     
                    inline Value lisp_gt(Value a, Value b) {
                        if (a.t == INT && b.t == INT) {
                            if (a.z.value > b.z.value) {
                                return MakeBoolean(1);
                            } else {
                                return MakeBoolean(0);
                            }
                        }
                        return *((Value *) NULL);
                    }
                    
                    inline Value lisp_lt(Value a, Value b) {
                        if (a.t == INT && b.t == INT) {
                            if (a.z.value < b.z.value) {
                                return MakeBoolean(1);
                            } else {
                                return MakeBoolean(0);
                            }
                        }
                        return *((Value *) NULL);
                    }
                    
                    inline Value lisp_eq(Value a, Value b) {
                        if (a.t == INT && b.t == INT) {
                            if (a.z.value == b.z.value) {
                                return MakeBoolean(1);
                            } else {
                                return MakeBoolean(0);
                            }
                        }
                        return *((Value *) NULL);
                    }
                    
                    Value lisp_and(Value a, Value b) {
                        if (a.t == BOOLEAN && b.t == BOOLEAN) {
                            if (a.b.value && b.b.value) {
                                return MakeBoolean(1);
                            } else {
                                return MakeBoolean(0);
                            }
                        }
                        return *((Value *) NULL);
                    }
                    
                    Value lisp_or(Value a, Value b) {
                        if (a.t == BOOLEAN && b.t == BOOLEAN) {
                            if (a.b.value || b.b.value) {
                                return MakeBoolean(1);
                            } else {
                                return MakeBoolean(0);
                            }
                        }
                        return *((Value *) NULL);
                    }
                    
                    Value lisp_not(Value a) {
                        if (a.t == BOOLEAN) {
                            if (!a.b.value) {
                                return MakeBoolean(1);
                            } else {
                                return MakeBoolean(0);
                            }
                        }
                        return *((Value *) NULL);
                    }
                    
                    // 
                    """);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
