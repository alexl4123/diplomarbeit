#!/bin/sh
	latex $ifn
	fn2="$(echo $ifn|sed s/.tex/.dvi/)"
	fn3="$(echo $ifn|sed s/.tex/-pics.pdf/)"
	  ### now dvipdf  $fn2 into $fn3 Container ...
	dvipdf $fn2  $fn3
	#dvipdf $(echo $ifn|sed s/.tex/.dvi/)
	  ### now pdflateXing $ifn ... (zweimal - fuers Inhaltsverzeichnis)
	pdflatex $ifn
pdflatex $ifn