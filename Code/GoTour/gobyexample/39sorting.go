package main

import (
	"fmt"
	"sort"
)

func main() {
	strs := []string{"c", "a", "b"}
	sort.Strings(strs)
	fmt.Println("sorted strings:", strs)

	ints := []int{7, 2, 4, 8, 0}
	s := sort.IntsAreSorted(ints)
	fmt.Println("sorted:", s)
	sort.Ints(ints)
	fmt.Println("sorted ints:", ints)

	s = sort.IntsAreSorted(ints)
	fmt.Println("sorted:", s)
}

/*
sorted strings: [a b c]
sorted: false
sorted ints: [0 2 4 7 8]
sorted: true
*/
