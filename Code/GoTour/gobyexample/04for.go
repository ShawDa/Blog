package main

import "fmt"

func main() {
	i := 1
	for i <= 3 {
		fmt.Println(i)
		i++
	}

	for i := 7; i <= 9; i++ {
		fmt.Println(i)
	}

	for true {
		fmt.Println("loop")
		break
	}

	for i := 0; i < 6; i++ {
		if i%2 == 0 {
			continue
		}
		fmt.Println(i)
	}
}

/*
1
2
3
7
8
9
loop
1
3
5
*/
