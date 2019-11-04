### 1.访问控制
| 访问权限 | 当前类 | 包 | 子类 | 其它包 |
|----|----|----|----|---- |
public | ∨	| ∨ | ∨ | ∨ |
protect | ∨ | ∨ | ∨ | × |
default	| ∨ | ∨ | × | × |
private	| ∨ | × | × | × |

### 2.final
final修饰基本类型的变量不能再被赋值，其值无法改变；final修饰引用类型变量时，可以保证变量不能被再次赋值，但对象值可能改变。

    ```java
    final StringBuilder sb = new StringBuilder("Hello");
    sb = new StringBuilder("hello");  // Cannot assign
    sb.append("World");  // OK
    ```