# This program computes and outputs the Bonferroni-p outliers of a time series generated by a linear model of flux 
# against a 2nd degree polynomial of time. The input format is a two column .csv (time, flux) and may include 
# restrictions on the indices to use to account for jumps in flux. After finding Bonferroni adjusted outliers at the 
# alpha = .05 level the element is removed and the test is conducted again with a new model. When the program is complete 
# a separate an ID line is printed into a separate file 

# The output is placed in a .csv file with two columns (outlier_time, outlier_bonferroniP)

args = commandArgs(trailingOnly=TRUE)
file_in <- args[1]
first_index <- args[2]
last_index <- args[3]
file_out <- args[4]
file_lock <- args[5]
lock_id <- args[6]

data = read.csv(file_in, header=FALSE)
time <- data$V1[first_index:last_index]
flux <- data$V2[first_index:last_index]
time_squared <- time * time

done <- FALSE
sink(file_out)
while(!done) {
	model = lm(flux ~ time + time_squared)

	# This is a terrible way to print out the label and know the index
	# TODO: Figure out how to get the index out of each test$bonf.p[i] value
	test_times = car::outlierTest(model, label=time)

	# TODO: Figure out in what format these elements are stored
	name <- names(test_times$bonf.p[1])
	value <- test_times$bonf.p[1]

	if(!is.na(name) & value < 1) {
		cat(name, ",", value, "\n", sep="", labels="")

		target_time <- as.integer(name)
		index_to_remove <- match(c(target_time), time)
		time <- time[-c(index_to_remove)]
		flux <- flux[-c(index_to_remove)]
		time_squared <- time_squared[-c(index_to_remove)]
	}
	else {
		done = TRUE
	}
}

sink(file_lock)
cat(lock_id, "\n")

