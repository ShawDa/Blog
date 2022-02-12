package main

import "fmt"

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
}
