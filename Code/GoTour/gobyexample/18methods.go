package main

import "fmt"

func main() {
	r := rect{10, 5}
	fmt.Println("area:", r.area())
	fmt.Println("perim:", r.perim())

	rp := &r
	fmt.Println("area:", rp.area())
	fmt.Println("perim:", rp.perim())
}

type rect struct {
	width, height int
}

func (r *rect) area() int {
	return r.width * r.height
}

func (r rect) perim() int {
	return r.height*2 + r.width*2
}

/*
area: 50
perim: 30
area: 50
perim: 30
*/
