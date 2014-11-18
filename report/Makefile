PDF_OUTPUT_FILE = report_ec27876_mt29253.pdf
ROOT_TEX = report.tex

TEXFILES := $(wildcard *.tex)
FIGFILES := $(wildcard figs/*.pdf)

all: clean $(PDF_OUTPUT_FILE)

$(PDF_OUTPUT_FILE): $(TEXFILES) $(FIGFILES) bibtex.bib
	pdflatex -shell-escape $(ROOT_TEX)
	bibtex report
	pdflatex $(ROOT_TEX)
	pdflatex $(ROOT_TEX)

clean:
	rm -rf *.aux *.log *.out *.bbl *.blg *.tod *.ilg *.tnd
