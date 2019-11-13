### 1.简介
**IntelliJ IDEA 是目前最好用的Java IDE ，没有之一。**

它主要用于支持 Java、Scala、Groovy 等语言的开发工具，同时具备支持目前主流的技术和框架，擅长于企业应用、移动应用和 Web 应用的开发。

JetBrains 公司旗下其它产品：
> * [PyCharm](http://www.jetbrains.com/pycharm/ "PyCharm 主要用于开发 Python") 主要用于开发 Python
> * [Android Studio](http://developer.android.com/tools/studio/ "Android Studio 主要用于开发 Android") 主要用于开发 Android
> * [PhpStorm](http://www.jetbrains.com/phpstorm/ "PhpStorm 主要用于开发 PHP") 主要用于开发 PHP
> * [RubyMine](http://www.jetbrains.com/ruby/ "RubyMine 主要用于开发 Ruby") 主要用于开发 Ruby
> * [AppCode](http://www.jetbrains.com/objc/ "AppCode 主要用于开发 Objective-C/Swift") 主要用于开发 Objective-C / Swift
> * [CLion](http://www.jetbrains.com/clion/ "CLion 主要用于开发 C/C++") 主要用于开发 C / C++
> * [WebStorm](http://www.jetbrains.com/webstorm/ "WebStorm 主要用于开发 JavaScript 等前端技术") 主要用于开发 JavaScript、HTML5、CSS3 等前端技术
> * [DataGrip](http://www.jetbrains.com/dbe/ "DataGrip 主要用于开发 SQL") 主要用于开发 SQL

### 2.常用快捷键
这些常用快捷键在 JetBrains 公司旗下的所有产品中基本一致，有些快捷键会和输入法等软件的起冲突，建议屏蔽其它软件中的。

#### Ctrl
|快捷键|介绍|
|:---------|:---------|
|<kbd>Ctrl</kbd> + <kbd>F</kbd>|在当前文件进行文本查找|
|<kbd>Ctrl</kbd> + <kbd>R</kdb>|在当前文件进行文本替换|
|<kbd>Ctrl</kbd> + <kbd>Z</kdb>|撤销一步|
|<kbd>Ctrl</kbd> + <kbd>Y</kdb>|删除光标所在行|
|<kbd>Ctrl</kbd> + <kbd>X</kdb>|剪切光标所在行 或 剪切选择的内容|
|<kbd>Ctrl</kbd> + <kbd>C</kdb>|复制光标所在行 或 复制选择的内容|
|<kbd>Ctrl</kbd> + <kbd>D</kdb>|复制光标所在行 或 复制选择内容，并把复制内容插入光标位置后面|
|<kbd>Ctrl</kbd> + <kbd>W</kdb>|递进式选择代码块。可选中光标所在的单词或段落，连续按会在原有选中的基础上再扩展选中范围，直到整个文件被选中|
|<kbd>Ctrl</kbd> + <kbd>E</kdb>|显示最近打开的文件记录列表|
|<kbd>Ctrl</kbd> + <kbd>N</kdb>|根据输入的 **类名** 查找类文件|
|<kbd>Ctrl</kbd> + <kbd>G</kdb>|在当前文件跳转到指定行处|
|<kbd>Ctrl</kbd> + <kbd>J</kdb>|插入自定义动态代码模板|
|<kbd>Ctrl</kbd> + <kbd>P</kdb>|方法参数提示显示，写代码时查看调用方法的参数情况|
|<kbd>Ctrl</kbd> + <kbd>Q</kdb>|光标所在的变量 / 类名 / 方法名等上面，显示文档内容|
|<kbd>Ctrl</kbd> + <kbd>U</kdb>|前往当前光标所在的方法的父类的方法 / 接口定义， 在看项目代码和源码是会经常用到|
|<kbd>Ctrl</kbd> + <kbd>B</kdb>|进入光标所在的方法/变量的接口或是定义处，等效于 `Ctrl + 左键单击` ，习惯后一种|
|<kbd>Ctrl</kbd> + <kbd>H</kdb>|显示当前类的层次结构|
|<kbd>Ctrl</kbd> + <kbd>O</kdb>|选择可重写的方法|
|<kbd>Ctrl</kbd> + <kbd>I</kdb>|选择可继承的方法|
|<kbd>Ctrl</kbd> + <kbd>\+</kdb>|展开代码|
|<kbd>Ctrl</kbd> + <kbd>\-</kdb>|折叠代码|
|<kbd>Ctrl</kbd> + <kbd>/</kdb>|注释光标所在行代码，会根据当前不同文件类型使用不同的注释符号|
|<kbd>Ctrl</kbd> + <kbd>\[</kdb>|移动光标到当前所在代码的花括号开始位置|
|<kbd>Ctrl</kbd> + <kbd>\]</kdb>|移动光标到当前所在代码的花括号结束位置|
|<kbd>Ctrl</kbd> + <kbd>F3</kdb>|调转到所选中的词的下一个引用位置|
|<kbd>Ctrl</kbd> + <kbd>F12</kdb>|弹出当前文件结构层，可以在弹出的层上直接输入，进行筛选|
|<kbd>Ctrl</kbd> + <kbd>Tab</kdb>|编辑窗口切换，如果在切换的过程又加按上delete，则是关闭对应选中的窗口|
|<kbd>Ctrl</kbd> + <kbd>End</kdb>|跳到文件尾|
|<kbd>Ctrl</kbd> + <kbd>Home</kdb>|跳到文件头|
|<kbd>Ctrl</kbd> + <kbd>Delete</kdb>|删除光标后面的单词或是中文句|
|<kbd>Ctrl</kbd> + <kbd>BackSpace</kdb>|删除光标前面的单词或是中文句|
|<kbd>Ctrl</kbd> + <kbd>光标定位</kdb>|按 Ctrl 不要松开，会显示光标所在的类信息摘要|
|<kbd>Ctrl</kbd> + <kbd>左方向键</kdb>|光标跳转到当前单词 / 中文句的左侧开头位置|
|<kbd>Ctrl</kbd> + <kbd>右方向键</kdb>|光标跳转到当前单词 / 中文句的右侧开头位置|
|<kbd>Ctrl</kbd> + <kbd>前方向键</kdb>|等效于鼠标滚轮向前效果|
|<kbd>Ctrl</kbd> + <kbd>后方向键</kdb>|等效于鼠标滚轮向后效果|

#### Alt
|快捷键|介绍|
|:---------|:---------|
|<kbd>Alt</kbd> + <kbd>\`</kbd>|显示版本控制常用操作菜单弹出层|
|<kbd>Alt</kbd> + <kbd>F3</kbd>|选中文本，按F3逐个往下查找相同文本，并高亮显示|
|<kbd>Alt</kbd> + <kbd>F7</kbd>|查找光标所在的方法 / 变量 / 类被调用的地方，Find Usages|
|<kbd>Alt</kbd> + <kbd>Enter</kbd>|IntelliJ IDEA 根据光标所在问题，提供快速修复选择，光标放在的位置不同提示的结果也不同|
|<kbd>Alt</kbd> + <kbd>Insert</kbd>|代码自动生成，如生成对象的 set / get 方法，构造函数，toString() 等|
|<kbd>Alt</kbd> + <kbd>前方向键</kbd>|当前光标跳转到当前文件的前一个方法名位置|
|<kbd>Alt</kbd> + <kbd>后方向键</kbd>|当前光标跳转到当前文件的后一个方法名位置|

#### Shift
|快捷键|介绍|
|:---------|:---------|
|<kbd>Shift</kbd> + <kbd>F2</kbd>|跳转到上一个高亮错误 或 警告位置|
|<kbd>Shift</kbd> + <kbd>F3</kbd>|在查找模式下，查找匹配上一个|
|<kbd>Shift</kbd> + <kbd>F4</kbd>|对当前打开的文件，使用新Windows窗口打开，旧窗口保留|
|<kbd>Shift</kbd> + <kbd>F6</kbd>|对文件 / 文件夹 重命名|
|<kbd>Shift</kbd> + <kbd>Tab</kbd>|取消缩进，可以简单理解为左退四个空格|
|<kbd>Shift</kbd> + <kbd>Enter</kbd>|开始新的空行，光标在新行行首|
|<kbd>Shift</kbd> + <kbd>左键单击</kbd>|在打开的文件名上按此快捷键，可以关闭当前打开文件|
|<kbd>Shift</kbd> + <kbd>滚轮前后滚动</kbd>|当前文件的横向滚动轴滚动，和拖动下面滚动条功能一致|

#### Ctrl + Alt
|快捷键|介绍|
|:---------|:---------|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>L</kbd>|格式化代码，自动标准化代码如等号前面两个空格变为一个，可以对当前文件和整个包目录使用|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>O</kbd>|优化导入的类，删除没用到的，可以对当前文件和整个包目录使用|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>T</kbd>|对选中的代码弹出环绕选项弹出层，如if-else，try-catch等|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>B</kbd>|在某个调用的方法名上使用会跳到具体的实现处，可以跳过接口，goto implementation|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>|打开 IntelliJ IDEA 系统设置|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>U</kbd>|类图快捷键，看源码很有用|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>F7</kbd>|显示使用的地方，寻找被该类或是变量被调用的地方，用弹出框的方式找出来|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Enter</kbd>|光标所在行上空出一行，光标定位到新行|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Home</kbd>|弹出跟当前文件有关联的文件弹出层|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>左方向键</kbd>|退回到上一个操作的地方|
|<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>右方向键</kbd>|前进到上一个操作的地方|

#### Ctrl + Shift
|快捷键|介绍|
|:---------|:---------|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>F</kbd>|根据输入内容查找整个项目 或 指定目录内文件里的文本内容|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>R</kbd>|根据输入内容替换对应内容，范围为整个项目 或 指定目录内文件|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>J</kbd>|自动将下一行合并到当前行末尾|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>Z</kbd>|取消撤销，有点类似于前进|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>N</kbd>|通过文件名定位 / 打开文件 / 目录，打开目录需要在输入的内容后面多加一个正斜杠|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>U</kbd>|对选中的代码进行大 / 小写轮流转换|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>T</kbd>|对当前类生成单元测试类，如果已经存在的单元测试类则可以进行选择|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>C</kbd>|复制当前文件磁盘路径到剪贴板|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>V</kbd>|弹出缓存的最近拷贝的内容管理器弹出层|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>E</kbd>|显示最近修改的文件列表的弹出层|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>I</kbd>|弹出一个窗口快速查看光标所在的方法 或 类的定义|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>A</kbd>|查找动作 / 设置|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>/</kbd>|代码块注释|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>\[</kbd>|选中从光标所在位置到它的顶部中括号位置|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>\]</kbd>|选中从光标所在位置到它的底部中括号位置|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>\+</kbd>|展开所有代码|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>\-</kbd>|折叠所有代码，这两项查代码行很有用|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>F12</kbd>|编辑器最大化|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>Backspace</kbd>|退回到上次修改的地方|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>左键单击</kbd>|把光标放在某个类变量上，按此快捷键可以直接定位到该类中|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>左方向键</kbd>|在代码文件上，光标跳转到当前单词 / 中文句的左侧开头位置，同时选中该单词 / 中文句|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>右方向键</kbd>|在代码文件上，光标跳转到当前单词 / 中文句的右侧开头位置，同时选中该单词 / 中文句|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>前方向键</kbd>|光标放在方法名上，将方法移动到上一个方法前面，调整方法排序|
|<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>后方向键</kbd>|光标放在方法名上，将方法移动到下一个方法前面，调整方法排序|

#### Alt + Shift
|快捷键|介绍|
|:---------|:---------|
|<kbd>Alt</kbd> + <kbd>Shift</kbd> + <kbd>左键双击</kbd>|选择被双击的单词 / 中文句，按住不放，可以同时选择其他单词 / 中文句，主要为了高亮部分数据|
|<kbd>Alt</kbd> + <kbd>Shift</kbd> + <kbd>前方向键</kbd>|移动光标所在行向上移动|
|<kbd>Alt</kbd> + <kbd>Shift</kbd> + <kbd>后方向键</kbd>|移动光标所在行向下移动|

## Others
|快捷键|介绍|
|:---------|:---------|
|<kbd>F2</kbd>|跳转到下一个高亮错误 或 警告位置|
|<kbd>F3</kbd>|在查找模式下，定位到下一个匹配处|
|<kbd>F4</kbd>|编辑源，走到光标所在方法的定义处编辑|
|<kbd>F11</kbd>|添加书签|
|<kbd>F12</kbd>|回到前一个工具窗口，如你之前打开了Terminal又关了，按下后又打开了|
|<kbd>Tab</kbd>|缩进|
|<kbd>Double Shift</kbd>|弹出 `Search Everywhere` 弹出层|