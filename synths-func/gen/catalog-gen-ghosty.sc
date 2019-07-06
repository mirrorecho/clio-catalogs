(

ClioLibrary.catalog ([\func, \gen, \ghosty], {

	~ghosty = ClioSynthFunc({ arg kwargs;

		var lf=LFNoise1.kr(freq:kwargs[\moanRate]);

		var lagFreq = Lag.kr(kwargs[\synth][\freq], kwargs[\lagTime]);

		var moanFreq = lagFreq*(kwargs[\freqMul]**lf);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + Pan2.ar(
			in:kwargs[\oscType].ar(freq:moanFreq, mul:kwargs[\ampScale] * 0.6),
			pos:LFNoise2.kr(kwargs[\panRate], mul:kwargs[\panMul]),
		);


	}, *[ // kwarg defaults:
		oscType:SinOsc,
		freqMul:1 + (1/8),
		moanRate:4,
		panRate:3,
		panMul:0.8,
		ampScale:1,
		lagTime:0.2,
	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)
