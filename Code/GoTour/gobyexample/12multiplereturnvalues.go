package main

import "fmt"

func main() {
	a, b, c := vals()
	fmt.Println(a, c, b)

	_, _, d := vals()
	fmt.Println(d)
}

func vals() (int, string, int) {
	return 1, "string", 3
}

/*
1 3 string
3
*/
