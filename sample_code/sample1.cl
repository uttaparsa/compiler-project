
class Main inherits IO {

    char : String;
    avar : A;
    flag : Bool <- true;

    main() : Object {
        {
            avar <- (new A);
            char <- "a";
            while flag loop
                {
                    if char = "j" then
                        avar <- (new A)
                    else
                        avar <- (new B)
                    fi;
                }
            pool;
        }
    };
};
class A {
    var : Int <- 0;
    set_var(num : Int) : SELF_TYPE{
        {
        var <- num;
        self;
        }
    };
    method2(num1 : Int, num2 : Int) : B { -- plus
        (let x : Int in
            {
                x <- num1 + num2;
                (new B).set_var(x);

            }
        )
    };
};
class B inherits A { --B is a number squared

    method5 (num : Int) : C{ -- square
        (let x : Int in
            {
                x <- num * num;
                (new C).set_var(x);
            }
        )
    };
};

class C inherits B {

    method (num: Int) :B { -- negate
        (let x: Int in
            {
                x <- ~num;
                (new B).set_var(x);
            }
        )
    };
};

