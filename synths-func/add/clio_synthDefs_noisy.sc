(

ClioLibrary.catalog ({

	~noiseHit1 = ClioSynthDefFactory(*[
		noiseType:BrownNoise,
		ampScale:1,
		out:Clio.busses[\master],

		// TO DO... think through kwargs

		{ arg name = \noiseHit1, kwargs;

			SynthDef(name, { arg freq=440, amp=0.4, rq=0.2316, // this is aprox BW of 1/3 octave, see: https://www.rane.com/note170.html
				attackTime=0.01, releaseTime=0.5, curve = -4,
				gate=1, out = kwargs[\out];

				var sig, env;

				sig = kwargs[\noiseType].ar(2!2) * (1/(rq**0.66));

				sig = BPF.ar(sig, freq, rq);

				env = EnvGen.kr(Env.perc(attackTime, releaseTime, amp * kwargs[\ampScale], curve), gate:gate, doneAction:2);

				Out.ar(out, sig*env);

			});

		};

	]);



}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)
