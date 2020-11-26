(

ClioLibrary.catalog ([\func, \add, \bell], {

	~toneBell1 = ClioSynthFunc({ arg kwargs;
		var
		freq=kwargs[\synth][\freq],
		amp=kwargs[\synth][\amp];

		var numFixedPartials=kwargs[\numFixedPartials];
		var numRandPartials=kwargs[\numRandPartials];

		var fixedFreqs = freq*(1..numFixedPartials);
		var fixedPartials = fixedFreqs.collect{|f|LFNoise2.ar(0.5!2).range(f*43/44, f*44/43)};
		var randFreqs = {ExpRand(freq, 18000)}!numRandPartials;
		var randPartials = randFreqs.collect{|f|LFNoise2.ar(1.1!2).range(f*68/69, f*69/68)};
		var partials = fixedPartials ++ randPartials;

		var fixedAmps = {amp/numFixedPartials*Rand(0.2,0.4)}!numFixedPartials;
		var randAmps = {amp/numRandPartials*Rand(0.1,0.3)}!numRandPartials;
		var amps = fixedAmps ++ randAmps;

		kwargs[\synth][\sig] = DynKlang.ar(
			`[partials,amps],
		);

	}, *[
		numFixedPartials:11,
		numRandPartials:33,
	]);



}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLibrary.
});

)

