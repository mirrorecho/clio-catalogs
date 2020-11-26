(

ClioLibrary.catalog ([\func, \add, \impulse], {

	// an impulse or impulses with a low pass filter
	// useful as input for ringer (Klank-based) synth functions
	~impulseLPF = ClioSynthFunc({ arg kwargs;

		var sig = Impulse.ar(kwargs[\impulseFreq],0,kwargs[\ampMul]!2);

		sig = LPF.ar(sig, kwargs[\lpfFreq]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + sig;

	}, *[ // set defaults:
		impulseFreq:0, // default is single impulse
		lpfFreq:8000,
		ampMul:1,
	]);

	// ditto as above, but with Dust instead of Impulse
	~dustLPF = ClioSynthFunc({ arg kwargs;

		var sig = Dust.ar(kwargs[\density],kwargs[\ampMul]!2);

		sig = LPF.ar(sig, kwargs[\lpfFreq]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + sig;

	}, *[ // set defaults:
		density:9, // default is single impulse
		lpfFreq:8000,
		ampMul:1,
	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLibrary.
});

)
