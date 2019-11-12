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
