uberscript := create-riddle

main: $(wildcard src/*.clj)
	echo ${DEPS_CP}
	bb --classpath ${DEPS_CP} --uberscript $(uberscript) src/clj/riddler/bb.clj
	echo "#!/usr/bin/env bb" | cat - $(uberscript) | tee $(uberscript) &> /dev/null
	chmod +x $(uberscript)

clean:
	[ -f $(uberscript) ] && rm $(uberscript)
