package com.movableink.app.data.model

import androidx.compose.runtime.Stable

@Stable
data class Product(
    val id: String,
    val imageUrl: String,
    val price: Long,
    val name: String,
    val gender: String,
    val category: String
)

val productList = listOf(
    Product(
        "118561",
        "w118561_alt_1000",
        4450,
        "Women's Levi's® 505™ Straight Jeans",
        "women",
        "jeans"
    ),
    Product(
        "1695762",
        "w1695762_alt_1000",
        4000,
        "Women's Gloria Vanderbilt Amanda Classic Tapered Jeans",
        "women",
        "jeans"
    ),
    Product(
        "1731442",
        "w1731442_alt_1000",
        4400,
        "Women's Plus Size Gloria Vanderbilt Amanda Classic Tapered Jeans",
        "women",
        "jeans"
    ),
    Product(
        "1833330",
        "m1833330_alt_1000",
        17900,
        "Men's Jean-Paul Germain Classic-Fit Microsuede Blazer",
        "men",
        "suitJackets"
    ),
    Product(
        "1878818",
        "w1878818_alt_1000",
        4500,
        "Women's Lee Modern Fit Curvy Bootcut Jeans",
        "women",
        "jeans"
    ),
    Product(
        "2263298",
        "w2263298_alt_1000",
        4000,
        "Women's Lee Relaxed Fit Straight Leg Jeans",
        "women",
        "jeans"
    ),
    Product(
        "2375187",
        "w2375187_alt_1000",
        4200,
        "Women's Simply Vera Vera Wang Rivet Denim Leggings",
        "women",
        "jeans"
    ),
    Product(
        "2403210",
        "m2403210_alt_1000",
        22000,
        "Men's Chaps Performance Classic-Fit Wool-Blend Comfort Stretch Suit Jacket",
        "men",
        "suitJackets"
    ),
    Product(
        "2426788",
        "m2447810_alt_1000",
        30000,
        "Men's Apt. 9® Slim-Fit Unhemmed Suit",
        "men",
        "dressSuit"
    ),
    Product(
        "2439667",
        "m2439667_alt_1000",
        22000,
        "Men's Chaps Classic-Fit Wool-Blend Performance Suit Jacket",
        "men",
        "suitJackets"
    ),
    Product(
        "2447810",
        "m2447810_alt_1000",
        30000,
        "Men's Croft & Barrow Classic-Fit Unhemmed Suit",
        "men",
        "dressSuit"
    ),
    Product(
        "2467840",
        "w2467840_alt_1000",
        3600,
        "Women's Briggs Comfort Waistband A-Line Skirt",
        "women",
        "skirts"
    ),
    Product(
        "2480979",
        "m2480979_alt_1000",
        24000,
        "Men's Marc Anthony Slim-Fit Stretch Suit Jacket",
        "men",
        "suitJackets"
    ),
    Product(
        "2481364",
        "w2481364_navy_romance_1000",
        3000,
        "Women's Tek Gear® Knit Workout Skort",
        "women",
        "skirts"
    ),
    Product(
        "2516619",
        "m2516619_berry_1000",
        3400,
        "Men's Croft & Barrow® Patterned Tie",
        "men",
        "ties"
    ),
    Product(
        "2527130",
        "m2527130_alt_1000",
        22000,
        "Men's Van Heusen Flex Slim-Fit Suit Jacket",
        "men",
        "suitJackets"
    ),
    Product(
        "2591560",
        "w2591560_alt_1000",
        7498,
        "Women's Chaps Pleated Sheath Dress",
        "women",
        "dresses"
    ),
    Product(
        "2599191",
        "m2599191_alt_1000",
        6000,
        "Men's Columbia Flattop Ridge Fleece Jacket",
        "men",
        "jackets"
    ),
    Product(
        "2608418",
        "m2608418_alt_1000",
        12999,
        "Men's Columbia Rockaway Mountain Interchange Systems Jacket",
        "men",
        "jackets"
    ),
    Product(
        "2694388",
        "m2694388_alt_1000",
        13499,
        "Men's Towne Wool-Blend Double-Breasted Peacoat with Plaid Scarf",
        "men",
        "jackets"
    ),
    Product(
        "2728436",
        "m2728436_alt_1000",
        22000,
        "Men's J.M. Haggar Premium Slim-Fit Stretch Suit Coat",
        "men",
        "suitJackets"
    ),
    Product(
        "2753275",
        "w2753275_alt_1000",
        3699,
        "Women's White Mark Solid Midi Skirt",
        "women",
        "skirts"
    ),
    Product(
        "2783785",
        "m2783785_alt_1000",
        18000,
        "Men's Apt. 9<sup>®</sup> Premier Flex Slim-Fit Suit Coat",
        "men",
        "suitJackets"
    ),
    Product(
        "2852223",
        "m2852223_alt_1000",
        8999,
        "Men's Columbia Wister Slope Colorblock Thermal Coil Insulated Jacket",
        "men",
        "jackets"
    ),
    Product(
        "2871342",
        "m2871342_alt_1000",
        3999,
        "Men's ZeroXposur Rocker Softshell Jacket",
        "men",
        "jackets"
    ),
    Product(
        "2874625",
        "m2874625_granite_heather_1000",
        3500,
        "Men's Champion Fleece Powerblend Top",
        "men",
        "shirts"
    ),
    Product(
        "2877645",
        "m2877645_alt_1000",
        9999,
        "Men's Columbia Rapid Excursion Thermal Coil Puffer Jacket",
        "men",
        "jackets"
    ),
    Product(
        "2881280",
        "w2881280_alt_1000",
        3500,
        "Women's Apt. 9® Embellished Bootcut Jeans",
        "men",
        "jeans"
    ),
    Product(
        "2898455",
        "m2898455_alt_1000",
        4999,
        "Men's Heat Keep Nano Modern-Fit Packable Puffer Jacket",
        "men",
        "jackets"
    ),
    Product(
        "2900935",
        "m2900935_buffalo_gray_1000",
        1998,
        "Men's Urban Pipeline® Awesomely Soft Ultimate Plaid Flannel Shirt",
        "men",
        "shirts"
    ),
    Product(
        "2939320",
        "m2939320_alt_1000",
        9399,
        "Men's Andrew Marc Wool-Blend Peacoat",
        "men",
        "jackets"
    ),
    Product(
        "2957101",
        "m2957101_alt6_1000",
        1499,
        "Men's Croft & Barrow® Classic-Fit Easy-Care Henley",
        "men",
        "shirts"
    ),
    Product(
        "2957114",
        "m2957114_new_white_1000",
        1698,
        "Men's Croft & Barrow® Classic-Fit Easy-Care Interlock Polo",
        "men",
        "shirts"
    ),
    Product(
        "2959247",
        "m2959247_blue_ombre_plaid_1000",
        1499,
        "Men's Croft & Barrow® True Comfort Plaid Classic-Fit Flannel Button-Down Shirt",
        "men",
        "shirts"
    ),
    Product(
        "2962920",
        "w2962920_alt_1000",
        5000,
        "Women's Simply Vera Vera Wang Skinny Jeans",
        "women",
        "jeans"
    ),
    Product(
        "2964013",
        "w2964013_alt_1000",
        3199,
        "LC Lauren Conrad Runway Collection Pleated Velvet Skirt - Women's",
        "wommen",
        "skirts"
    ),
    Product(
        "2964826",
        "w2964826_alt_1000",
        2499,
        "Women's Apt. 9® Tummy Control Pull-On Pencil Skirt",
        "women",
        "skirts"
    ),
    Product(
        "2971290",
        "m2971290_alt_1000",
        6400,
        "Men's Chaps Classic-Fit Corduroy Stretch Sport Coat",
        "men",
        "suitJackets"
    ),
    Product(
        "2972771",
        "m2972771_alt_1000",
        6400,
        "Men's Van Heusen Flex Slim-Fit Sport Coat",
        "men",
        "suitJackets"
    ),
    Product(
        "2980246",
        "m2980246_red_large_check_1000",
        1499,
        "Men's Croft & Barrow® Arctic Fleece Quarter-Zip Pullover",
        "men",
        "shirts"
    ),
    Product(
        "2982007",
        "w2982007_alt_1000",
        4000,
        "Women's Wallflower Luscious Curvy Bootcut Jeans",
        "women",
        "jeans"
    ),
    Product(
        "2984389",
        "w2984389_alt_1000",
        4000,
        "Women's Tek Gear® Hooded Long Sleeve Dress",
        "women",
        "dresses"
    ),
    Product(
        "2995056",
        "w2995056_alt_1000",
        1499,
        "Women's Croft & Barrow® Essential Ribbed Turtleneck Sweater",
        "women",
        "sweaters"
    ),
    Product(
        "2999682",
        "w2999682_alt_1000",
        2499,
        "Women's Apt. 9® Cozy Shawl Collar Cardigan",
        "men",
        "sweaters"
    ),
    Product(
        "3003041",
        "w3003041_alt_1000",
        2999,
        "Women's Apt. 9® Fit & Flare Dress",
        "men",
        "dresses"
    ),
    Product(
        "3009587",
        "m3009587_alt_1000",
        3699,
        "Men's New Balance Sherpa-Lined Polar Fleece Hooded Jacket",
        "men",
        "jackets"
    ),
    Product(
        "3021766",
        "w3021766_alt_1000",
        1499,
        "Women's SONOMA Goods for Life™ Lattice Sweater",
        "women",
        "sweaters"
    ),
    Product(
        "3024658",
        "m3024658_alt_1000",
        9999,
        "Men's Free Country 3-in-1 Systems Jacket",
        "men",
        "jackets"
    ),
    Product(
        "3028158",
        "w3028158_alt_1000",
        2499,
        "Women's Apt. 9® Lace Yoke A-Line Dress",
        "men",
        "dresses"
    ),
    Product(
        "3036618",
        "m3036618_sweet_lavender_1000",
        1199,
        "Big & Tall Croft & Barrow® Classic-Fit Easy-Care Interlock Polo",
        "men",
        "shirts"
    ),
    Product(
        "3040746",
        "m3040746_camo_alt_1000",
        8800,
        "Women's Rock & Republic® Fever Denim Rx™ Pull-On Jean Leggings",
        "men",
        "jeans"
    ),
    Product(
        "3054934",
        "w3054934_alt_1000",
        3899,
        "Chaps Women's Metallic Faux-Suede Pleated Midi Skirt",
        "women",
        "skirts"
    ),
    Product(
        "3057086",
        "w3057086_alt_1000",
        3200,
        "Women's Dana Buchman Mitered Cowlneck Sweater Dress",
        "women",
        "dresses"
    ),
    Product(
        "3063706",
        "w3063706_alt_1000",
        8199,
        "Women's Chaps Satin Trim Jersey Dress",
        "women",
        "dresses"
    ),
    Product(
        "567950",
        "m567950_alt_1000",
        7998,
        "Men's Croft & Barrow® True Comfort Classic-Fit Sport Coat",
        "men",
        "suitJackets"
    ),
    Product(
        "825463",
        "m825463_soho_gray_1000",
        3200,
        "Men's Croft & Barrow® Classic-Fit Easy Care Point-Collar Dress Shirt",
        "men",
        "suitJackets"
    ),
    Product(
        "c1193954",
        "mc1193954_1000",
        15000,
        "Men's Haggar Travel Tailored-Fit Performance Suit Separates",
        "men",
        "dressSuit"
    ),
    Product(
        "c1193957",
        "mc1193957_1000",
        14500,
        "Men's Marc Anthony Slim-Fit Stretch Suit Separates",
        "men",
        "dressSuit"
    ),
    Product(
        "c1569952",
        "mc1569952_1000",
        14000,
        "Men's Apt. 9® Premier Flex Extra-Slim Fit Suit Separates",
        "men",
        "dressSuit"
    ),
    Product(
        "c1621950",
        "mc1621950_1000",
        18000,
        "Men's J.M. Haggar Premium Slim-Fit Stretch Suit Separates",
        "men",
        "dressSuit"
    ),
    Product(
        "c1672950",
        "mc1672950_1000",
        13000,
        "Men's Apt. 9® Slim-Fit Stretch Suit Separates",
        "men",
        "dressSuit"
    ),
    Product(
        "c1672951",
        "mc1672951_1000",
        13500,
        "Men's Apt. 9® Extra-Slim Fit Stretch Suit Separates",
        "men",
        "dressSuit"
    ),
    Product(
        "c890950",
        "mc890950_1000",
        15999,
        "Men's Chaps Performance Classic-Fit Wool-Blend Stretch Suit Separates",
        "men",
        "dressSuit"
    )
)
