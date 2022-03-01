package main

import "fmt"

func main() {
	var intArray [3]int64
	intArray[0] = 0
	intArray[1] = 1
	intArray[2] = 2
	fmt.Println(intArray)         // [0 1 2]
	fmt.Printf("%p\n", &intArray) // 0xc000010420
	// 0xc000010420 0xc000010428 0xc000010430
	fmt.Println(&intArray[0], &intArray[1], &intArray[2])

	var numArray0 = [3]int{1, 2, 3}
	fmt.Println(numArray0) // [1 2 3]

	numArray1 := [...]int{4, 5, 6}
	fmt.Println(numArray1) // [4 5 6]

	numArray2 := [...]int{2: 9, 1: 8, 0: 7}
	fmt.Println(numArray2) // [7 8 9]

	for i := 0; i < len(numArray2); i++ {
		fmt.Println(numArray2[i])
	}

	for i, num := range numArray2 {
		fmt.Println(i)
		fmt.Println(num)
	}

	testPointerArray(&numArray2)
	fmt.Println(numArray2[2]) // 100
}

func testPointerArray(array *[3]int) {
	(*array)[2] = 100
}
