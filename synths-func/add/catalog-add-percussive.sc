(

// the functions here
// adds percussive elements to a SynthDef, such as noisy initial strike or bass-drum like tones

ClioLibrary.catalog ([\func, \add, \percussive], {


	// rand tones from sine waves
	// at low freqs with perc env this works as a bass drum
	//at mid/high freqs it's a funny alien sounding thingy
	// with many tones, it has a cymbal-like sound
	~drumy = ClioSynthFunc({ arg kwargs;

		var randFreqs, randAmps, tones, sig;
		var freq = kwargs[\synth][\freq];

		randFreqs = { ExpRand(freq/kwargs[\freqSpread], freq*kwargs[\freqSpread]) }!kwargs[\numTones];
		randAmps = { Rand(0.6*kwargs[\ampMul]/kwargs[\numTones], 1.2*kwargs[\ampMul]/kwargs[\numTones]) }!kwargs[\numTones];

		tones = Klang.ar(`[randFreqs, randAmps]);

		sig = Splay.ar(tones, kwargs[\tonesSplay]) * AmpComp.kr(freq, 90);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + sig;

	}, *[// set default kwargs:
		numTones:9,
		tonesSplay:0.4,
		freqSpread:1.5,
		ampMul:1,
	]);

	// =====================================================================================

	// WARNING: THESE noiseHit synths DO NOT FREE THE SYNTH
	// (most common use would be to combine with other functions that do free the synth)
	// ... so if they're used independently, they should be combined with DetectSilence

	~noiseHitLow1 = ClioSynthFunc({ arg kwargs;

		var env, sig;

		sig = LPF.ar(kwargs[\oscType].ar(0.9!2), kwargs[\lpfFreq]);
		env = EnvGen.kr(Env.perc(
			attackTime:kwargs[\attackTime],
			releaseTime:kwargs[\releaseTime],
			level:kwargs[\ampMul],
			curve:kwargs[\curve])
		);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + (sig * env);

	}, *[ // set default kwargs:
		oscType:PinkNoise,
		lpfFreq:244,
		ampMul:0.9,
		attackTime:0.01,
		releaseTime:2,
		curve:-8,
	]);

	// =====================================================================================

	~noiseHitHi1 = ClioSynthFunc({ arg kwargs;

		var env, sig;

		sig = HPF.ar(kwargs[\oscType].ar(0.9!2), kwargs[\hpfFreq]);
		env = EnvGen.kr(Env.perc(
			attackTime:kwargs[\attackTime],
			releaseTime:kwargs[\releaseTime],
			level:kwargs[\ampMul],
			curve:kwargs[\curve])
		);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + (sig * env);

	}, *[ // set default kwargs:
		oscType:PinkNoise,
		hpfFreq:6000,
		ampMul:0.9,
		attackTime:0.001,
		releaseTime:1,
		curve:-16,
	]);

	// =====================================================================================

	~noiseHitResonz1 = ClioSynthFunc({ arg kwargs;

		var env, sig;
		var freq = kwargs[\synth][\freq];

		sig = Resonz.ar(kwargs[\oscType].ar(kwargs[\ampMul]!2), freq*2, 0.4, 2) * AmpComp.kr(freq, 60, 0.69);
		env = EnvGen.kr(Env.perc(
			attackTime:kwargs[\attackTime],
			releaseTime:kwargs[\releaseTime],
			level:kwargs[\ampMul],
			curve:kwargs[\curve])
		);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + (sig * env);

	}, *[ // set kwarg defaults
		oscType:WhiteNoise,
		ampMul:0.9,
		attackTime:0.01,
		releaseTime:1,
		curve:-16,
	]);

	// =====================================================================================

}
);

)
