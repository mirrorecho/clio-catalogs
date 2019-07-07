(

ClioLibrary.catalog ([\func, \gen, \poppy], {

	~poppy = ClioSynthFunc({ arg kwargs;

		var sig = Resonz.ar( kwargs[\oscType].ar( 1!kwargs[\numTones] ),
			freq: kwargs[\synth][\freq]*kwargs[\freqMul],
			bwr: kwargs[\bwr],
			mul:( 0.8/(kwargs[\bwr]**0.8) ) * kwargs[\ampMul],
		);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + Splay.ar(sig, spread:kwargs[\splay]);


	}, *[ // kwarg defaults:
		oscType:PinkNoise,
		numTones:8,
		bwr:0.04, // smaller is quieter and more pitch-focused
		splay:0.6,
		ampMul:1,
		freqMul:1
	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)
