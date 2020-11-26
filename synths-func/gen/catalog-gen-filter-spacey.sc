(

ClioLibrary.catalog ([\func, \gen, \spacey], {

	// NOTE: needs DetectSilence
	~rlpfSpacey = ClioSynthFunc({ arg kwargs;

		var freq = kwargs[\synth][\freq];
		var sig = kwargs[\oscType].ar(freq:freq, mul:kwargs[\ampMul]);
		sig = RLPF.ar(sig, kwargs[\noiseOscType].kr(kwargs[\resonFreqRate]!2).range(freq*2, 9900), 0.2);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + sig;

	}, *[
		oscType:Pulse, // Saw can also be a good choise
		ampMul:1,
		noiseOscType:LFNoise1,
		resonFreqRate:8,
	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)

