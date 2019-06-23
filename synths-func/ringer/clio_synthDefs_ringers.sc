(

ClioLibrary.catalog ({

	~ringer = ClioSynthDefFactory(*[
		attackTime:0.001,
		decayTime:0.2,
		sustainLevel:1,
		releaseTime:2,
		curve: -4,
		overtones:[1, 2, 3, 4, 5, 6, 7, 8],
		overtoneAmps:[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		ringTimes:[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		// buffer: Clio.buffers['japan-cicadas', '0159-insects-in-kyoto'], // TO DO... consider having some default buffers around?
		rate:1,
		start:0,
		randStart:0,
		mix:1.0,
		centerFreq:440,
		rateScale:0, // scales rate of buffer
		ampScale:1,
		channels:2,
		out:Clio.busses[\master],

		{ arg name = \ringer, kwargs;

			SynthDef(name, { arg amp=1, freq=440, out = kwargs[\out];

				var
				buffer = \buffer.ir(kwargs[\buffer]),
				attackTime = \attackTime.ir(kwargs[\attackTime]),
				releaseTime = \releaseTime.ir(kwargs[\releaseTime]),
				rate = \rate.ir(kwargs[\rate]),
				start = \start.ir(kwargs[\start]),
				randStart = \randStart.ir(kwargs[\randStart]), // randomize seconds up to this value to add to sample start
				mix = \mix.ir(kwargs[\mix]),
				curve = \curve.ir(kwargs[\curve]),
				centerFreq = \centerFreq.ir(kwargs[\centerFreq]),
				rateScale = \rateScale.ir(kwargs[\rateScale]);

				var sig, bufsig, env;
				var specsArray = [
					kwargs[\overtones]*freq, // FREQS
					kwargs[\overtoneAmps], // AMPLITUDES
					kwargs[\ringTimes] // RING TIMES
				];

				env = EnvGen.ar(Env.perc(attackTime, releaseTime, level:amp*kwargs[\ampScale], curve:curve), doneAction:2);

				bufsig = PlayBuf.ar(kwargs[\channels],
					bufnum:buffer,
					rate:BufRateScale.kr(buffer) * rate * ((freq/centerFreq)**rateScale),
					startPos:BufSampleRate.kr(buffer) * (start + Rand(0, randStart)),
					// doneAction:2, // NOTE: better to keep synth going until end of env so that buffer can be used as impulse
				);

				sig = Klank.ar(`specsArray, bufsig) * AmpComp.kr(freq, 20, 0.2);
				sig = (sig*mix) + (bufsig*(1-mix));
				sig = sig * env;

				Out.ar(out, sig);

			});

		};

	]);


	~ringerDrone = ClioSynthDefFactory(*[
		attackTime:0.001,
		decayTime:0.2,
		sustainLevel:1,
		releaseTime:2,
		curve: -4,
		overtones:[1, 2, 3, 4, 5, 6, 7, 8],
		overtoneAmps:[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		ringTimes:[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		// buffer: Clio.buffers['japan-cicadas', '0159-insects-in-kyoto'], // TO DO... consider having some default buffers around?
		rate:1,
		mix:1.0,
		centerFreq:440,
		rateScale:0, // scales rate of buffer
		ampScale:1,
		channels:2,
		out:Clio.busses[\master],

		{ arg name = \ringerDrone, kwargs;

			SynthDef(name, { arg amp=1, freq=440, gate=1, out = kwargs[\out];

				var
				buffer = \buffer.ir(kwargs[\buffer]),
				attackTime = \attackTime.ir(kwargs[\attackTime]),
				decayTime = \decayTime.ir(kwargs[\decayTime]),
				sustainLevel = \sustainLevel.ir(kwargs[\sustainLevel]),
				releaseTime = \releaseTime.ir(kwargs[\releaseTime]),
				rate = \rate.ir(kwargs[\rate]),
				mix = \mix.ir(kwargs[\mix]),
				curve = \curve.ir(kwargs[\curve]),
				centerFreq = \centerFreq.ir(kwargs[\centerFreq]),
				rateScale = \rateScale.ir(kwargs[\rateScale]);


				var specsArray = [
					kwargs[\overtones]*freq, // FREQS
					kwargs[\overtoneAmps], // AMPLITUDES
					kwargs[\ringTimes] // RING TIMES
				];


				var bufsigs, bufsig, bufenvs, sig, env;

				var length = BufDur.ir(buffer);
				var envTimes;

				rate = rate * ((freq/centerFreq)**rateScale);
				envTimes = (length / rate / 4)!3;

				bufsigs = 4.collect{ |i|
					PlayBuf.ar(kwargs[\channels],
						bufnum:buffer,
						rate:BufRateScale.ir(buffer)*rate,
						startPos:BufSampleRate.ir(buffer) * length * i/4,
						loop:1);
				};

				bufenvs = [
					EnvGen.kr(Env.new(levels: [0, 0.25, 0.5, 0.25, 0], times: envTimes, curve: [6,-6,6,-6,]).circle),
					EnvGen.kr(Env.new(levels: [0.25, 0.5, 0.25, 0, 0.25], times: envTimes, curve: [-6,6,-6,6]).circle),
					EnvGen.kr(Env.new(levels: [0.5, 0.25, 0, 0.25, 0.5], times: envTimes, curve: [6,-6,6,-6]).circle),
					EnvGen.kr(Env.new(levels: [0.25, 0, 0.25, 0.5, 0.25], times: envTimes, curve: [-6,6,-6, 6,]).circle),
				];
				bufsigs = bufsigs * bufenvs;
				bufsig = Mix.ar(bufsigs);

				sig = Klank.ar(`specsArray, bufsig) * AmpComp.kr(freq, 20, 0.2);
				sig = (sig*mix) + (bufsig*(1-mix));

				sig = sig * EnvGen.kr(
					Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, peakLevel:amp*kwargs[\ampScale],
					),
					gate:gate, doneAction:2

				);

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
