(

ClioLibrary.catalog ([\func, \gen, \ghosty], {


	~ghost = ClioSynthFunc({ arg kwargs;
		var sig, sig2, env;
		var freq = Lag.kr( kwargs[\synth][\freq], kwargs[\slideTime]);
		sig = Resonz.ar(Crackle.ar(1.98!2), freq, 0.04, 24) +
		Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 12) +
		Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 6) +
		Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 4) +
		Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 2);
		kwargs[\synth][\sig] = kwargs[\synth][\sig] + Splay.ar(sig, spread:0.9);
	}, *[ // kwarg defaults:
		slideTime:0.8,
	]);


	~ghostMoan = ClioSynthFunc({ arg kwargs;

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
