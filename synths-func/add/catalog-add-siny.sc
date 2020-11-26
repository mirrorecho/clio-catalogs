(

ClioLibrary.catalog ([\func, \add, \sin], {

	Vibrato.ar(



	// TO DO: separate/parameterize the meaningful components of this out the meaning
	~vibrato = ClioSynthFunc({ arg kwargs;
		var sig = 0;
		var freq = Lag.kr(kwargs[\synth][\freq], kwargs[\slideTime]);
		var amp =  Lag.kr(kwargs[\synth][\amp], kwargs[\slideTime]);

		sig = sig + Logistic.ar(chaosParam: 3.8!2, freq:freq, init: 0.5, mul: 0.04, add: 0);
		sig = sig + Logistic.ar(chaosParam: 3.9!2, freq:freq*2, init: 0.5, mul: 0.03, add: 0);
		sig = sig + Logistic.ar(chaosParam: 3.99!2, freq:freq*4, init: 0.5, mul: 0.02, add: 0);

		sig = sig + BHiPass.ar(
			Crackle.ar(chaosParam: 2!2, mul: 0.1, add: 0),
			freq, 0.08, 6
		);

		sig = sig + Resonz.ar(
			Crackle.ar(chaosParam: 2!2, mul: 0.1, add: 0),
			freq*6, 0.4, 4
		);

		// TO DO... move to processing funcs?
		sig = sig + Crackle.ar(chaosParam: 2!2, mul: 0.04, add: 0);

		sig = sig + Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 8); // DO TO: ???

		sig = sig + Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 6); // DO TO: ???

		sig = sig + Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 4); // DO TO: ???

		sig = sig * kwargs[\ampMul];

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + sig;

	}, *[ // set defaults:
		slideTime:0.01,
		ampMul:1,
		rq:0.2316, // this is aprox BW of 1/3 octave, see: https://www.rane.com/note170.html
	]);

}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLibrary.
});

)

