package main

import "fmt"

func main() {
	sumInt := intSeq()

	fmt.Println(sumInt())
	fmt.Println(sumInt())
	fmt.Println(sumInt())

	newInt := intSeq()
	fmt.Println(newInt())

	seq := testSeq()
	fmt.Println(seq)
	fmt.Println(seq)
	fmt.Println(seq)
}

func testSeq() int {
	num := 0
	for i := 0; i <= 10; i++ {
		num += i
	}
	return num
}

func intSeq() func() int {
	num := 0
	return func() int {
		for i := 0; i <= 10; i++ {
			num += i
		}
		return num
	}
}

/*
55
110
165
55
55
55
55
*/
