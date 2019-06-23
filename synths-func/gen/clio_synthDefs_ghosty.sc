(

ClioLibrary.catalog ({

	~ghosty = ClioSynthDefFactory(*[
		oscType:SinOsc,
		freqMul:1 + (1/8),
		moanRate:4,
		panRate:3,
		panMul:0.8,
		ampScale:1,
		attackTime:0.1,
		decayTime:0.3,
		sustainLevel:1.0,
		releaseTime:0.5,
		out:Clio.busses[\master],

		{ arg name = \ghosty, kwargs;

			kwargs[\out].postln;

			SynthDef(name, { arg amp=0.2, gate=1, freq=440; //, out = kwargs[\out];
				var sig, env;
				var freqMul = \freqMul.kr(kwargs[\freqMul], 0.5);
				var lf=LFNoise1.kr(freq:kwargs[\moanRate]);
				var lagFreq = Lag.kr(freq, 0.2);
				var moanFreq = lagFreq*(freqMul**lf);
				var out = \out.ir( kwargs[\out]);
				var attackTime = \attackTime.ir( kwargs[\attackTime] );
				var sustainLevel = \sustainLevel.ir( kwargs[\sustainLevel] );
				var releaseTime = \releaseTime.ir( kwargs[\releaseTime] );
				var decayTime = \decayTime.ir( kwargs[\decayTime] );

				sig = Pan2.ar(
					in:kwargs[\oscType].ar(freq:moanFreq, mul:amp * kwargs[\ampScale] * 0.6),
					pos:LFNoise2.kr(kwargs[\panRate], mul:kwargs[\panMul]),
				);

				env = EnvGen.kr(Env.adsr(attackTime:attackTime, decayTime:decayTime, sustainLevel:sustainLevel, releaseTime:releaseTime), gate:gate, doneAction:2);

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
