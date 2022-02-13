package main

import (
	"fmt"
	"strings"
)

func paraPointer(num *int) {
	*num = *num + 10
	fmt.Printf("num after add : %v\n", *num) // 20
}

func funcAsVariable(a int, b int) (c int) {
	c = a + b
	return
}

func funcAsFuncVar(funcVar func(int2 int, int3 int) int, a int, b int) int {
	return funcVar(a, b)
}

func sumArgs(a int, args ...int) (b int) {
	b = a
	for i := 0; i < len(args); i++ {
		b += args[i]
	}
	return
}

var GlobalAnonymousFunc = func(m int, n int) int {
	return m * n
}

// 闭包就是一个函数和与其相关引用环境组合的一个整体
func closePackage() func(int) int {
	n := 0
	return func(i int) int {
		n += i
		return n
	}
}

func addNumAndString() func(int) (int, string) {
	n := 10
	str := "test"
	return func(i int) (int, string) {
		n += i
		str += "a"
		return n, str
	}
}

func addSuffixIfNotExist(suffix string) func(string) string {
	return func(s string) string {
		if !strings.HasSuffix(s, suffix) {
			return s + suffix
		}
		return s
	}
}

func main() {
	num := 10
	fmt.Printf("num before paraPointer : %v\n", num) // 10
	paraPointer(&num)
	fmt.Printf("num after paraPointer : %v\n", num) // 20

	funcVariable := funcAsVariable
	fmt.Println(funcVariable(100, 99)) // 199

	fmt.Println(funcAsFuncVar(funcVariable, 199, 1)) // 200

	fmt.Println(sumArgs(15221))                         // 15221
	fmt.Println(sumArgs(1, 2, 3, 4, 5, 6, 9, 10, 8, 7)) // 55

	anonymous := func(m int, n int) int {
		return m + n
	}(123, 456)
	fmt.Println(anonymous) // 579

	anonymousFunc := func(m int, n int) int {
		return m - n
	}
	fmt.Println(anonymousFunc(2, 1)) // 1
	fmt.Println(anonymousFunc(1, 2)) // -1

	fmt.Println(GlobalAnonymousFunc(3, 9)) // 27

	closePackageVar := closePackage()
	fmt.Println(closePackageVar(3)) // 3
	fmt.Println(closePackageVar(2)) // 5
	fmt.Println(closePackageVar(1)) // 6

	addNumAndStringVar := addNumAndString()
	fmt.Println(addNumAndStringVar(3)) // 13 testa
	fmt.Println(addNumAndStringVar(2)) // 15 testaa
	fmt.Println(addNumAndStringVar(1)) // 16 testaaa

	addSuffixIfNotExistVar := addSuffixIfNotExist(".png")
	fmt.Println(addSuffixIfNotExistVar("a.png")) // a.png
	fmt.Println(addSuffixIfNotExistVar("b"))     // b.png
}
