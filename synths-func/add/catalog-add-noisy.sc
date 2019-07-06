(

ClioLibrary.catalog ([\func, \add, \noisy], {

	~noiseBand1 = ClioSynthFunc({ arg kwargs;

		var sig, env;

		sig = kwargs[\oscType].ar((2*kwargs[\ampScale])!2) * (1/(kwargs[\rq]**0.66));

		sig = BPF.ar(sig, kwargs[\synth][\freq], kwargs[\rq]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + sig;

	}, *[ // set defaults:
		oscType:BrownNoise,
		ampScale:1,
		rq:0.2316, // this is aprox BW of 1/3 octave, see: https://www.rane.com/note170.html
	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLibrary.
});

)

