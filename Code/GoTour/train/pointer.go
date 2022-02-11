package main

import (
	"fmt"
)

func main() {
	num := 1
	var pointer = &num
	// addr of num 0xc00000a090
	fmt.Println(pointer)
	// addr of pointer 0xc000006028
	fmt.Println(&pointer)
	// value of num 1
	fmt.Println(*pointer)
	// set value of pointer addr 100
	*pointer = 100
	// 0xc00000a090
	fmt.Println(pointer)
	// 0xc000006028
	fmt.Println(&pointer)
	// num is 100
	fmt.Println(num)
}
