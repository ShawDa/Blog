package main

import "fmt"

func sum(m int, n int) int {
	// 当执行到defer时，暂时不执行，会将defer后面的语句压入到独立的栈（defer栈）
	// 当函数执行完毕后, 返回值之前，再从defer栈中取出执行
	defer fmt.Println(2)

	res := m + n
	fmt.Println(1)
	return res
}

func sumMore(m int, n int) int {
	defer fmt.Println(m)
	defer fmt.Println(n)

	m++
	n++
	return m + n
}

func main() {
	fmt.Println(sum(123, 456)) // 1 2 579
	res := sum(122, 211)
	fmt.Println(res) // 1 2 333

	// defer将语句放入栈的时候，也会将相关值拷贝入栈
	fmt.Println(sumMore(12, 34)) // 34 12 48
}
