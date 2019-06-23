(

ClioLibrary.catalog ({

	~play = ClioSynthDefFactory(*[
		rate:1.0,
		start:0,
		amp:1,
		out:Clio.busses[\master],

		{ arg name = \bufPlay, kwargs;

			SynthDef(
				name, {arg out = kwargs[\out];

					var buffer = \buffer.ir( kwargs[\buffer] );
					var start = \start.ir( kwargs[\start] );
					var rate = \rate.kr( kwargs[\rate] );
					var amp = \amp.kr( kwargs[\amp] );

					var sig = PlayBuf.ar(2,
						bufnum:buffer,
						rate:BufRateScale.kr(buffer) * rate,
						startPos:BufSampleRate.ir(buffer) * start,
						doneAction:2,
					);

					sig = sig * amp;

					Out.ar(out, sig);
			});


		};

	]);

	~perc = ClioSynthDefFactory(*[
		rate:1.0,
		start:0,
		amp:1,
		attackTime:0.01,
		releaseTime:2.0,
		curve:-4,
		out:Clio.busses[\master],

		{ arg name = \bufPerc, kwargs;

			SynthDef(
				name, {arg out = kwargs[\out];

					var buffer = \buffer.ir( kwargs[\buffer] );
					var start = \start.ir( kwargs[\start] );
					var rate = \rate.kr( kwargs[\rate] );
					var amp = \amp.kr( kwargs[\amp] );

					var sig = PlayBuf.ar(2,
						bufnum:buffer,
						rate:BufRateScale.ir(buffer) * rate,
						startPos:BufSampleRate.ir(buffer) * start,
						doneAction:2,
					);

					var attackTime = \attackTime.ir( kwargs[\attackTime] );
					var releaseTime = \releaseTime.ir( kwargs[\releaseTime] );
					var curve = \curve.ir( kwargs[\curve] );

					var env = Env.perc(attackTime:attackTime, releaseTime:releaseTime, level:amp, curve:curve);
					sig = sig * EnvGen.ar(env, doneAction: 2);

					Out.ar(out, sig);
			});


		};
	]);

}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)




