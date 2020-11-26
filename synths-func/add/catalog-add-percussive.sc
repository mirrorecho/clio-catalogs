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


	// default values are "U"-ish
	// USEFUL VARIANTS:
	// (
	// 	U: [formant1: 250, formant2: 595, formant_amp:0.04],
	// 	O: [formant1: 360, formant2: 640, formant_amp:0.01],
	// )
	// TO DO: a better implementation of this might be to use klank to add formants to any signal
	// TO DO MAYBE: formants as their own synth function?
	~formantTom = { arg kwargs, noiseAmt=0.5, formant1=250, formant2=595, formantAmp=0.04;

		var sig, lpf_freq;

		var formant_count = kwargs[\def_formant_count] ? 3;
		var formant_spread1 = kwargs[\def_formant_spread1] ? 1.04;
		var formant_spread2 = kwargs[\def_formant_spread2] ? 1.02;
		var noise_spread = kwargs[\def_noise_spread] ? 1.1;

		var freq = kwargs[\freq]; // simply to save typing

		var formant_freqs = ExpRand((formant1/formant_spread1)!formant_count, (formant1*formant_spread1)!formant_count)
		++ ExpRand((formant2/formant_spread2)!formant_count, (formant2*formant_spread2)!formant_count);

		var formant_amps = (formantAmp!formant_count)++((formantAmp/2)!formant_count) * kwargs[\amp];

		lpf_freq = noiseAmt * 16;

		sig = (LPF.ar(Resonz.ar(WhiteNoise.ar(1.0!2), ExpRand(freq, freq*noise_spread), noiseAmt/4.0, 20/noiseAmt), freq*8)
			+ LPF.ar(Resonz.ar(WhiteNoise.ar(1.0!2), ExpRand(freq, freq/noise_spread), noiseAmt/4.0, 20/noiseAmt), lpf_freq*8))
		* AmpComp.kr(freq, 90, 0.1)
		+ Splay.ar(Klang.ar(`[formant_freqs, formant_amps, nil]), 0.3);

		kwargs[\sig] = kwargs[\sig] + sig;

	};





}
);

)
