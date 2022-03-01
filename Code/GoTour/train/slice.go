package main

import "fmt"

func main() {
	ints := [...]int{1, 2, 3, 5, 8, 13, 21}
	// 引用ints数组，起始下标为1，终止下标为3（不包括）
	slice := ints[1:3]
	fmt.Println(slice)      // [2 3]
	fmt.Println(len(slice)) // 2
	fmt.Println(cap(slice)) // 6

	makeSlice := make([]int, 5, 10)
	makeSlice[1] = 10
	makeSlice[3] = 100
	fmt.Println(makeSlice)      // [0 10 0 100 0]
	fmt.Println(len(makeSlice)) // 5
	fmt.Println(cap(makeSlice)) // 10

	makeSlice1 := makeSlice[1:4]
	fmt.Println(makeSlice1)      // [10 0 100]
	fmt.Println(len(makeSlice1)) // 3
	fmt.Println(cap(makeSlice1)) // 9

	makeSlice2 := append(makeSlice1, 1000, 10000, 100000)
	fmt.Println(makeSlice2)      // [10 0 100 1000 10000 100000]
	fmt.Println(len(makeSlice2)) // 6
	fmt.Println(cap(makeSlice2)) // 9

	array := []int{1, 2, 3, 4, 5}
	copySlice := make([]int, 10)
	copy(copySlice, array)
	fmt.Println(array)     // [1 2 3 4 5]
	fmt.Println(copySlice) // [1 2 3 4 5 0 0 0 0 0]

	str := "helloworld"
	strSlice := str[5:]
	fmt.Println(strSlice) // world
}

// 引用类型，底层数据结构如下
type mySlice struct {
	ptr *[2]int
	len int
	cap int
}
