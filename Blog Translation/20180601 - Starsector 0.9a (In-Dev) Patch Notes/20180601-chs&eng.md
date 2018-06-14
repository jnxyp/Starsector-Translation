# [Starsector 0.9a (In-Dev) Patch Notes](http://fractalsoftworks.com/forum/index.php?topic=13445.0)
# 远行星号0.9a（开发中）更新日志

[**Chinese Version 中文版本**](20180601-chs.md)

Changes as of June 01, 2018 by **`Alex`** in `Forum Announcements`

Translated at June 01, 2018 by **`Jn_xyp`, `陌鸢`**

于2018年6月1日翻译，译者：**`Jn_xyp`，`陌鸢`**

## Campaign  生涯模式

- Colony building:
- 殖民地建设：
	- Player can establish a colony on a planet they've surveyed
	- 玩家可以在他们已探索的星球上建立殖民地
	- Requires crew, machinery, and supplies
	- （建设殖民地）需要船员，重型机械和补给
	- Can change the planet's name, either when colony is established or at any point later on
	- 可以在殖民地建立后任意更改行星名称
	- Can build "Industries" and "Structures" (same thing, mechanically) at a colony
	- 可以在殖民地建设“工业设施”和“结构”（实际上是一样的）
	- Some example ones 一些可以建造的东西:
		- Mining
		- 矿场
		- Farming
		- 农场
		- Spaceport
		- 星港
		- Heavy Industry
		- 重工业（造船）
		- Orbital stations (of several varieties)
		- 轨道站（包含多种）
		- Military Base
		- 军事基地
		- Tech-Mining
		- 科研机构
		- ... and a number of other ones
		- ...以及其他许多类型
	- Some industries can be upgraded, i.e. "Patrol HQ" -> "Military Base", or "Heavy Industry" -> "Orbital Works"
	- 一些工业设施可以被升级，例如“巡逻站”->“军事基地”，“重工工厂”->“轨道工业”
	- Most industries are known from the start, but a few more exotic ones can be learned
	- 大部分工业设施在游戏开始就可用，但还有一些特殊的设施需要学习才成建造
	- Can set a "stockpiling level" for a colony to have it build up extra resources the player can take
	- 可以为殖民地设定一个“储备等级”，以令殖民地为玩家生产更多可用的资源
		- Stockpiling costs credits, but somewhat less than the base cost of the commodities
		- 虽然进行资源储备需要花费资金，但仍然比市面上的商品的基础价格更加实惠
		- Stockpiles will be used to counter temporary shortages
		- 资源储备将会被用以应对暂时性的资源短缺
	- Player colonies have a "local resources" submarket where they can take from, or add to, stockpiles. There's a "storage" submarket for resources that must remain untouched.
	- 玩家的殖民地将会拥有一个“本地资源”子市场，玩家可以从中拿取或向其中储存资源。殖民地另有一个“仓储”子市场，用于存储那些不能被动用的资源。
	- Player colonies do **not** have an Open Market unless there's a "Commerce" industry
	- 玩家的殖民地**不会**有对外开放的市场，除非殖民地建设了“商贸”行业设施
	- Colonies start at size 3; population growth depends on many factors and can be directly invested into
	- 殖民地的起始市场规模为3；人口的增长取决于多种因素，并且可以通过投入移民来直接增加
	- Can assign AI cores to manage industries for various, significant benefits
	- 可以分配AI核心来管理工业设施，以获取多种多样的重要加成
	- A Military Base produces patrols which will defend the colony
	- 军事基地可以生产用于保卫殖民地的巡逻队
	- Can hire Administrators (up to a limit) to manage your colonies
	- 可以雇佣管理者（有上限）来管理你的殖民地
	- Can learn skills that improve the colonies under your control
	- 可以通过学习技能来改进你手下的殖民地
		- Maximum number under personal control is soft-limited - reduced stability for going beyond
		- 个人控制的殖民地数量有一个软性上限，超出数量上限将会导致（殖民地）稳定性下降
	- Can assign Alpha Cores to manage your colonies, for a massive benefit
	- 可以将Alpha 级人工智能核心分配用于管理殖民地，可以获得巨大的加成
	- Establishing the first colony also creates a new player faction
	- 第一个殖民地的建立将会创建一个新的玩家势力
	- Can set the faction name and select a flag
	- 可以为玩家势力指定名称和旗帜
	- Can adjust both at any point in the future
	- 在未来可随时对两者进行修改

- Blueprints for ship hulls, weapons, and fighter LPCs:
- 舰船船体、武器和舰载机的蓝图：
	- Required by Heavy Industry to build ships
	- 需要重工业来建设船只
		- Ship production "quality" is based on many factors and affects the number of d-mods a produced hull will have
		- 舰船的“生产质量”取决于许多因素，并会影响生产出舰船所包含的d船插个数
		- Can be improved by certain items found during exploration
		- （“生产质量”）可以被星系探索中发现的特定物品所提高
	- Some basic blueprints always known from the start, rest have to be acquired
	- 一些基础的蓝图在游戏开局就可用，而其余的则需要后来得到
	- Blueprints can be prioritized to get the desired fleet composition and weapon/fighter use
	- 为某些种类的蓝图设定优先级来生成你所要的舰队、武器和战机组合
	- Affect fleet production by player colonies
	- 影响玩家殖民地的舰队生产
	- Player can custom-order known ships and weapons
	- 玩家可以自主订购已知（蓝图）的舰船和武器
		- Will be built based on the monthly production capacity of their heavy industry
		- （订单的）建造（时间）基于玩家重工业的月生产容量（进行计算）
		- And delivered to a designated gathering point, along with some crew, fuel, and supplies
		- 并将（订购的物品）与一些船员、燃料和补给送到指定的集结点

- New economy system to support colony mechanics
- 全新的经济系统以支持殖民地机制
	- Player buying/selling has direct impact on market's availabpiracyle commodity numbers
	- 玩家的购买和出售行为将会对市场的可用商品数量造成直接的影响
	- Player colonies can become suppliers for other factions and generate export income for the player
	- 玩家的殖民地可以成为其他势力的（商品）供应商，并为玩家产生出口收入
		- Relative "accessibility" of colonies determines who the best supplier is
		- 殖民地间的相对“可到达性”决定了谁是最佳的（商品）供应商
		- Player has several tools to manage this, from establishing waystations to piracy
		- 玩家有多种工具和手段来改变（殖民地的可到达性），包括但不限于建立中途站和拦路打劫

- Officers found in sleeper pods can now be up to level 15 instead of 5
- 现在从（漂流的）休眠仓中获得的军官等级上限可达15级（过去为5级）

- Administrators can now be found in sleeper pods; likeliest place to find high-skilled ones
- （殖民地）管理者也可以在休眠舱中被寻获；（休眠舱）是最有可能找到高技能等级管理者的地方
	- Can now go over the maximum number of officers and administrators
	- 可以（通过捡休眠舱）拥有超过上限数量的军官和管理者
		- Can't use the ones over maximum
		- 不能真正使用超过上限数量的军官
		- Can't hire to go over maximum
		- 不能雇佣超过上限数量的军官
		- Mainly useful to be able to find officers/admins in sleeper pods, while already at maximum
		- 主要是为了保证在军官和管理员数量已满的情况下，仍然可以从休眠舱中寻获军官

- Factions 
- 势力
	- Factions have access to ships/weapons/fighters based on their blueprints
	- 一个势力可以建造它们自己蓝图中的舰船、武器和战机
	- AI ship loadouts are dynamically generated, based on what's available
	- AI舰船的装配现在将会根据可用的（蓝图）动态进行生成
	- Factions have been adjusted so that their available blueprints and doctrine make each more distinct
	- 对（原版）派系进行了调整，以使它们的可用蓝图和势力理念更加明确
		- Examples:
		- 例如：
		- The Hegemony favors larger ships and high-quality officers
		- 霸主更加偏好大型的船只和高质量的军官
		- The Luddic Church, while also mostly drawing from low-tech designs:
		- 路德教会，其大部分（的舰船）都来自于低技术的设计
			- Favors larger numbers of ships with lower-quality officers
			- 偏重于更多的舰船的数量和低质量的军官
			- Makes extensive use of Converted Hangars
			- 大量使用“改装机库”船插
			- Has access to a faction-specific `Perdition-class` torpedo bomber
			- 添加了势力限定的`咒灭-级`（暂定名称，待议）鱼雷轰炸机

- Faction doctrine: settings that affect faction fleet composition and ship behavior
- 势力理念：影响势力舰队组成和行为的设定
	- Each setting can go from 1 to 5
	- 每一种设定都有介于1到5之间的权重点数
	- Warships - proportion of regular combat ships found in fleets, on average
	- 普通战斗舰船 - 常规战舰在舰队中的平均占比
	- Carriers - proportion of carriers
	- 航母 - 航母占比
	- Phase Ships - proportion of phase ships
	- 相位船 - 相位船占比
	- Warships, Carriers, and Phase Ships must add up to 7 points
	- 三种舰船占比的权重点之和为7
	- Officer Quality, Ship Quality, and More Ships - must also add up to 7 points
	- 军官质量、舰船质量和舰船数量的权重点之和也为7
	- Ship Size - independent setting, determines average size of ships preferred
	- 舰船尺寸 - 独立设定，决定了舰队舰船的平均尺寸
	- Aggression - determines personality of officers; does not affect the player's own fleet
	- 侵略性 - 决定了军官的性格
		- Also applies to combat personality/behavior of ships without officers, both for the player and for other factions
		- 同时也为玩家和不同势力的舰船添加了在无军官状态下的对战性格和行为
		- This aspect **does** affect the player's own fleet
		- 这一设定**会**作用于玩家的舰队
	- Each faction now has an appropriate doctrine setting
	- 每一种势力现在有一套适合的理念设定

- Monthly income and expenses:
- 每月收入和支出
	- Outposts/colonies
	- 前哨站和殖民地
		- Base income from a population
		- 从人口中得来的基础收入
		- Upkeep for each industry; based on hazard rating of planet
		- 每个工业设施的维护费用是基于其所在星球的危险等级而计算
			- A lower-hazard planet can support more industries on the "base income"; exact numbers still TBD
			- 一个更为宜居的星球可以用其基础收入支撑更多的工业设施，准确的数值仍然待定
			- Can go above that for a negative income
			- （殖民地）的工业设施维护费用可以超过该上限，但将会产生负的收入
		- Exports: income for becoming the best provider of a commodity to another faction's colony
		- 出口：通过成为其他势力殖民地某种商品的最佳提供者来获得收入
			- Incurs "overhead" when there are many exports from the same colony
			- 如果一个殖民地大量地进行出口，将会产生额外的间接成本
			- **Always** better to have more exports, but the added benefit drops off quickly
			- **增加出口**总是好的，但当出口数量增加时，（商品）的利润会很快下降
	- Storage - 1% of base value of stored ships/cargo
	- 仓储量 - 已存储舰船和货物基础值的1%
	- Crew, officer, and administrator salaries
	- 船员、指挥官和管理者的工资
	- Presented in a detailed monthly report
	- （以上内容）将会在详细的月度报告中展现

- New tab: `Command`, shortcut `D`. Allows player to:
- 新的界面标签：`指挥`（暂定名称），快捷键`D`，玩家可以：
	- Manage colonies
	- 管理殖民地
	- Can also see which markets you have storage at and what's stored where
	- 可以查看哪一个空间站有存储的货物，以及货物内容
	- View latest income report
	- 查看最新的收入报告
	- Manage the doctrine and blueprint priorities
	- 管理（势力的）理念和蓝图优先级
	- Make custom production orders
	- 定制生产订单

- Cargo screen
- 仓储界面
	- Can quick-transfer (or sell) a lot of cargo by holding down Alt then mousing over the stacks
	- 可以通过摁住Alt键并在货物堆上移动鼠标来快速地转移或售卖大量的货物
		- Can be turned off via `settings.json`
		- 可通过修改`settings.json`来关闭
	- Improved quantity selection slider for picking up part of a stack
	- 改进过的批量选择滑条，用于选择一个货物堆的一部分
	- Supplies/fuel/crew/marines/heavy machinery now shown first in sorted cargo; other commodities also shown before weapons/fighter chips/etc
	- 补给、燃料、船员、陆战队员、重型机械现在在排序后的货物中首先显示；其他商品也会显示在武器、战机芯片和其它物品之前
	
- Revamped `Intel` screen
- 重新设计的`网络信息`界面
	- Tag-based filtering system
	- 基于标签的筛选系统
	- Always shows map
	- 总是展示星图
	- UI is updated based on the current state of missions and such
	- 用户界面将会根据当前的任务状态而改变
	- Can accept and abandon certain missions directly from the intel screen
	- 可以直接在此同意或放弃任务（无需前往星球或空间站）
	- Can flag pieces of intel as "important"
	- 可以将某些信息标记为“重要”
	- Intel is now "live" and does not require a new message for the information displayed to change
	- 现在此界面上显示的信息是“实时”的，也就是说已有信息的改变将不会显示为一条新的信息
	
- Person bounties:
- 个人悬赏：
	- Will now pay out and increase reputation when hostile or worse with offering faction
	- 完成敌对势力的任务将会得到赏金并提高与该势力的关系。
	- Information given re: where to find target now includes constellation and some planet/star system type hints, but no actual star system unless it's a lone star
	- 悬赏信息的修改：关于如何找到目标的提示现在包含所在星座和星系、行星类型的说明。除非目标处于一个只有恒星的星系，否则确切的星系名字将不会被给出。
	- Reward no longer based on market stability
	- 报酬不再与市场稳定性有关

- System bounties:
- 星系赏金：
	- Will now pay out and increase reputation when hostile or worse with offering faction
	- 完成敌对势力的任务将会得到赏金并提高与该势力的关系。
	- Most likely in systems containing markets hostile to each other
	- 更容易出现在同时存在敌对势力市场的星系

- Faction commissions:
- 势力雇佣：
	- Have to talk to a high-ranking faction official to get one
	- 必须与该势力高级别的官员进行交流以建立雇佣关系
	- Pays a level-based monthly stipend in addition to a small bounty for enemy ships
	- 佣金包括根据等级发放的月度补贴和摧毁敌对势力舰船获得的少量赏金
	- Partially restores standing with other factions as hostility status changes
	- 当（所在势力）与其他势力的敌对状态改变时，部分地恢复与其他势力的关系
	- Can be resigned; smaller reputation penalty if in person
	- 可以辞职；如果亲自递交辞呈将会减少因此造成的关系下降
		- Restores standing with factions that became hostile as a result of holding the commission
		- （辞职）将会结束与其它势力因雇佣行为而造成的敌对

- Faction hostilities: fixed various issues re: hostilities properly ending and total number/frequency
- 势力敌对：修复多个问题，调整敌对结束的方式和（关系改变的）次数和频率
	- Last for at least 6 months, and rarely more than a full cycle
	- （敌对）持续至少6个月，但很少超过一年
	- Hostilities between Hegemony-TriTachyon and other starting hostilities between major factions (i.e. not pirates/pathers) are no longer permanent
	- 主要势力在游戏开局时的敌对（例如霸主和速子之间的敌对，不包括海盗或任务舰队）不再是永久的了
	
- Smugglers now have a low reputation impact if attacked, i.e. won't turn entire faction instantly hostile
- 现在攻击（其他势力的）走私者只会产生相对较小的敌对影响，不会立刻与其整个实力进入敌对状态

- Chance to have advance intel about departures of trade fleets/smugglers, including timeframe and cargo
- 有机会提前了解到贸易或走私舰队的到达和离境情况，包括具体的时间和所携带的货物

- Changed options for colony interaction
- 更改了（玩家）与殖民地间的互动选项
	- Can open all core UI tabs with corresponding shortcut instead of just a subset
	- 现在可以通过相应的快捷键展开所有的核心UI标签，而不是仅仅其中一个子菜单
	- Added "visit dockside bar" option; details are WIP
	- 添加了“去码头边上逛一圈”选项，具体细节待定
	- Added "Consider your military options" option; sub-menu allows player to attack orbital station
	- 添加了“考虑你的军事选择”选项，其子菜单允许玩家攻击轨道站
		- Will also contain a few TBD mechanics re: planet-based military actions
		- 将会包括一些目前待定的机制，比如关于对于星球的军事行动

- Dropping cargo pods may now distract pirate fleets; duration of distraction (if any) depends on quantity of goods ejected
- 将货物抛出船舱现在可能会分散海盗的注意力；而使海盗分心的时间则取决于货物的质量

- Autoresolve (AI vs AI, and auto-resolved pursuit battles):
- （战斗结果）自动计算（AI间的战斗，以及副官带领的追击战）：
	- Takes d-mods/production quality into account
	- 舰船的d-船插和生产质量现在被纳入计算
	- Works with stations and modular ships
	- 空间站和多模块舰船现在也被纳入计算

- Made various improvements to "where is this entity" mission/bounty/etc description text
- 对于在赏金、任务和其它事件中描述“这个东西在哪”的描述文本进行了众多改进

- Toned down bounty level-scaling
- 调低了赏金任务的目标等级赏金加成

- Added "Compromised Storage" dmod, reduces cargo/crew/fuel capacity of ship by 30%
- 添加了“仓储妥协”d船插

- Added to Mudskipper Mk.2
- 将“仓储妥协”d船插添加到了 `弹涂鱼Mk.2` 上

- Added `Gremlin-class` lowtech phase frigate, with pirate and Luddic Path versions
- 添加了 `捣蛋鬼-级`（暂定名称，待议）低技术相位护卫舰，有海盗和路德左径两个版本

- Buy and sell cost of ships now includes the base supply cost of their current combat readiness (i.e., no/minimal credit loss for recovering CR on a ship and then selling it)
- 舰船的买卖现在包含其恢复战备所需的基础补给支出，例如在出售一艘舰船前，需要消耗一些星币来恢复其战备
	- Similar CR-based modifier added to supplies etc recovered when scuttling a ship
	- 当选择凿沉一艘舰船时，所得到的补给也会受到该船战备值的影响

- Added two new torpedo bomber wings: `Perdition` and `Cobra`
- 添加了两种鱼雷轰炸机：`咒灭` 和 `眼镜蛇`（暂定名称，待议）

- Fleet AI:
- 舰队AI：
	- Much less easily sidetracked by chases; patrols in particular will largely focus on defending their objectives
	- 巡逻舰队将会把工作重心放在保卫其守卫的目标上，并大大减少了它们为了追逐目标而不务正业的可能性
	- When orbiting a planet, will spread out to try to avoid overlap with other orbiting fleets
	- 当舰队轨道环绕星球飞行时，将会自动散开以避免重叠

- Reduced number of campaign ship contrails; improves performance a lot and should generally not be noticeable
- 减少了舰队航迹的数量，极大地提高了性能，并且通常不易被察觉

- Sensors: combined detection range in hyperspace now limited to 2000 units (was 5000)
- 传感器：超空间中的（舰队）组合探测距离现在被限制到2000单位（原为5000）

- Added "generate name" option to colony renaming dialog
- 为殖民地重命名对话框添加了“随机生成名称”选项

- Installed fighters now shown in the fleet screen
- 安装（在舰船）上的战机联队现在会被显示在舰队管理界面上

- Trade fleet routes will stop by a waystation if appropriate
- 在合适的情况下，贸易舰队会在途中的驿站进行停留

- Fighting independent scavenger fleets will now only slightly reduce standing with the independents
- 与独立的打捞舰队之间的战斗仅会轻微地降低与自由联盟间的关系

- Temporary/event-based market conditions no longer have "event" label, grouped to the left of other conditions instead
- 临时的或是基于事件的市场环境条件将不会显示为“事件”，而是与其它市场条件一并显示在左侧
	- Condition icon size scales down if there are too many to fit in the available width
	- 表示市场条件的图标在图标过多、横向空间不足时将会自动缩小尺寸

- Lava planets will no longer show up as part of the combat background (too bright)
- 熔岩行星将不会在战斗中作为背景（亮瞎眼）

- Jump-point tooltip no longer shows planet types in unexplored star systems
- 对于未探索的星系，跳跃点上显示的星球列表将不再会包含星球的类型

- Number of d-mods is now shown for each fleet member in the campaign fleet tooltip
- 现在舰队所包含的每艘舰船的d船插数量将会显示在舰船列表上

- D-mods are now also highlighted in the ship tooltip's list of hullmods
- d船插现在也会被高亮显示在舰船的船插列表中

- Made "fleet despawning" animation smoother
- 使“舰队消失”的动画更加流畅

- Number of supplies UI indicator will now include the picked-up supply stack, if any
- 补给数量指示器现在也会包括拾取到的补给堆（如果有的话）

- When a course is laid in and you approach a jump-point, selecting "leave" will also disable autopilot
- 在已经规划了自动驾驶路线，并接近一个跳跃点的时候，选择“离开”选项也会取消自动驾驶

## Miscellaneous 杂项

- Updated to use newer version of xstream (1.4.10)
- 更新了对更新版本xstream（1.4.10）的支持

- Cleaned up controls screen - updated text and removed commands that are no longer in the game
- 清理控制面板 - 更新文本并删除游戏中不再使用的的命令

- Made some improvements to game-saving code that should speed it up on non-SSD drives
- 一定程度上优化了游戏存档代码，尤其是针对非SSD设备。

- Made in-combat "exit game" button red to avoid potential confusion with "claim victory"
- 在战斗中把“退出游戏”按钮变成红色以防止与“宣布胜利”按钮的误触

- Vertical scrollbar active area now wider, indicator expands when mouse is in active area
- 竖直滚动条变得更宽，鼠标在激活状态下会变大

- Improved algorithm for automatically generating weapon groups
- 自动生成武器组的改进算法

- Improved game startup speed when sound is enabled by up to around 40% total, depending on the system specs
- 根据系统情况提高了40%的游戏启动速度当开启声音的时候

## Orbital Stations 空间站

- Can be built at a colony to provide defenses and support nearby friendly fleets in combat
- 现在可以建造防御设施在殖民地附近，并且其还可以用来支援友军舰队
       
- Existing colonies have these as appropriate
- 殖民地可以非法占有一下设施

- Three tiers: Orbital Station, Battlestation, and Star Fortress
- 三个等级：空间站，战斗堡垒和星形堡垒

- Three tech levels/styles: low tech, midline, and high tech
- 三种科技术等级/风格：低级，中级和高级

- Roughly similar combat power of each style, but different tactical approaches
- 每个级别有相似的战斗实力但是不同的战术手段

- Dynamically fitted with weapons based on faction weapon availability
- 基于势力武器能力动态武器装配系统

- Higher-tier stations are bolstered with drones and minefields
- 通过战机连队和雷区来提升空间站等级

## Combat 战斗

- Improved proximity fuse range detection vs longer/thinner ships
- 优化了对于细长船型的优化碰撞优化

- Phase ships:
- 相位船
	- Doom: new ship system, "Mine Strike", spawns high-damage, high-delay proximity fuse mines
	— 厄运：增加了一个新的“鱼雷打击”舰船技能，它可以发射高伤害，高延迟的“引信者”鱼雷
			- Huge buff overall
			- 大的整体加成（buff）
	- Harbinger: system changed to Quantum Disruptor
	- 预言者：改为量子干扰系统
	- Afflictor: system changed to Entropy Amplifier
	- 折磨：改为熵放大器系统

- Various performance improvements, should be around 20-30% faster
- 各种性能增加 20%~30% 左右

- Toggling autofire on on an already-selected weapon group will make that group autofire
— 在已选择的武器组上切换自动开火会使盖组武器自动开火
	- Can override and get manual control by selecting the group again
	- 可以通过重复选择武器组来覆盖之前选择并手动控制

- Fixed bug that caused the fighter rate replacement multiplier to apply twice
- 修复了战斗机修复并替换重复的错误（bug）

- New graphics for Annihilator Pod medium weapon
- 歼灭者火箭发射舱有新贴图啦 ~

- Greatly improved missile tracking
- 导弹追踪系统大大改善
	- And fixed issues with ECCM sometimes making it worse; it should now be a significant improvement
	- 修复了电子对抗（ECCM）对其的干扰，现在应该有有大幅改善

## Ship AI

- Fixed issue that was causing small weapon groups made up of hardpoints to not be set to autofire
- Will no longer fire low-ammo missiles vs overloaded fighters
- Fixed issue with too many carriers auto-escorting nearby combat ships
- Reduced tendency of carriers to assign their fighter wings to escort nearby ships instead of attacking
- Carriers assigned a "search & destroy" order will no longer use nearby ships to hide behind
- Improved logic for multiple ships not using too many total missiles vs a single target
- Fixed issue that was causing carriers to order fighters to "engage" too far from target, causing unnecessary loss of the 0-flux boost
- Fixed issue that was causing carriers with some support fighters to not order the other fighters to engage
- Fixed bug that caused a retreating ship with front shields to keep them on when it was not necessary
- More likely to set smaller groups to autofire when flux is low, including hardpoint groups
- Improved autofire flux management
	- Will autofire most kinds of weapon groups, depending on flux level and dissipation
	- Able to keep on pressure with low-flux weapons when on high flux
- Improved flux management for high-flux weapons such as Heavy Blasters
- Fixed bug that was causing fighters to only rarely fire missiles/torpedoes at low-hitpoint targets
- Improved omni shield use vs phase ships
- Fixed bug that was causing the AI to improperly evaluate where it has the most remaining armor
- Ships gradually get more aggressive when their peak time and CR tick down

## Modding

- Fixed crash from left-clicking station in hyperspace
- Error message due to incorrectly configured music set is now comprehensible
- Ships with modules:
	- Modules are no longer configured using weapon groups
	- Added "modules" section to .variant files instead
	- Modules will be ordered in listed order provided "modules" is an array of objects
- Station/ship-with-module weapons no longer rendered in separate pass above everything
	- Use module order to control rendering order instead
- Variants, hulls, skins, skills, weapons, projectiles: will be merged with core files when loaded
	- Meaning, a mod can now provide a partial file with just the changes rather than a complete file
- Renamed FleetDespawnListener to FleetEventListener
- Added reportBattleOccurred() method to FleetEventListener
- Fixed bug to do with beamFireOnlyOnFullCharge and venting while firing at max charge
- Proximity fuse projectiles with a trigger range of 0 will now only explode at end of range
- MutableStat values no longer capped to be >= 0
- Added better error message when game crashes due to an invalid ship system id being specified
- Added MissileAPI.interruptContrail() - needs to be called twice, once at old and once at new location
- Added MarketAPI.get/setEconGroup()
	- Markets within the same econ group only trade with each other and are not visible when viewing nearby markets etc from a market in a different group. Default group is null (the value, not the string).
- Added ShipAIPlugin.getConfig() method that returns a ShipAIConfig (or null if a custom ship AI doesn't support the core config parameters)
- Added "tags" and "rarity" columns to ship_data.csv
	- Skins do NOT inherit tags from the parent ship, but can have their own tags specified via a "tags" json array
	- Default d-hulls also do not inherit any tags from the base hull
- Added "tech/manufacturer" column to ship and weapon data
	- Defaults to "Common" if unspecified
	- "manufacturer" key to override in ship skin file
- SectorEntityTokens and MarketAPIs stored in a MemoryAPI will be replaced by their ids in save files, and restored on load
	- For this and other reasons, code must gracefully handle a case where an entity is put into memory but then is no longer there or *is* there but is no longer in any LocationAPI
- Removed dHullOverlaySmall/Large from hull_styles.json; replaced with single dHullOverlay, which should be large-ish and will tile to cover bigger hulls (instead of being upscaled)
- hull_styles.json: now 3 d-mod overlays per style; light/medium/heavy, in only one size
	- d-mod overlays tile for larger ships
- Moved get/setModuleVariant() methods from FleetMemberAPI to ShipVariantAPI
- Weapon group generation:
	- Added "GROUP_LINKED" and "GROUP_ALTERNATION" AI hints to weapon_data; will attempt to place weapons with those in separate groups and set them to linked or alternating
	- Added groupTag column to weapon_data; when set, will heavily favor placing weapons with same groupTag into the same group, without other weapons in it
	- Will not auto-generate weapon groups for AI fleets until combat starts, for performance reasons
- Fixed word-wrapping issue when a single word is too long for a line
- Added limited support for drones with SHIP collision class; will no longer crash into mothership
- Added "canUseWhileRightClickSystemOn" to ship system definitions
- Missiles now support the "jitter" effect, above the missile sprite only
- Changes to fleet despawn logic: listeners will now be notified before the fleet member list is cleared
	- Allows seeing fleet state at time of despawn if it wasn't snapshotted
	- Fleet members removed afterwards
- Added CollisionGridAPI that gives access to internal bin-lattice used to speed up collision checks/AI
	- void addObject(Object object, Vector2f loc, float objWidth, float objHeight);
	- void removeObject(Object object, Vector2f loc, float objWidth, float objHeight);
	- Iterator<Object> getCheckIterator(Vector2f loc, float checkWidth, float checkHeight);
- Added to CombatEngineAPI:
	- CollisionGridAPI getAllObjectGrid();
	- CollisionGridAPI getShipGrid();
	- CollisionGridAPI getMissileGrid();
	- CollisionGridAPI getAsteroidGrid();
- Added to SettingsAPI:
	- String readTextFileFromCommon(String filename)
	- void writeTextFileToCommon(String filename, String data)
	- Load/save text data from the <installdir>/saves/common/ folder
		- Limited to 1MB per file, 50MB total files per session
- Hull mods tagged with "no_drop_salvage" will not drop from campaign salvage operations
	- Different from "no_drop", which will only make them not drop from defeated ships in combat
- Added alternate system for listening to campaign events, see: ListenerManagerAPI
	- Allows registering to listen for only one type of event
	- Not everything converted to use it, but will only use this system going forward
- Added event-listening interfaces:
	- ShowLootListener
	- SurveyPlanetListener
	- EconomyTickListener
	- DiscoverEntityListener
- Added IntelManagerAPI to manage player-visible pieces of information
	- See: Global.getSector().getIntelManager()
	- Replaces EventManager etc
	- CommMessageAPI, SectorAPI.reportEventStage, etc deprecated/removed
	- Game no longer loads/uses reports.csv
	- Missions are re-implemented as "IntelInfoPlugin" rather than a events
	- A few events that were essentially scripts (such as OfficerManagerEvent) remain for legacy reasons
	- MissionBoard and related APIs removed
	- All of these are now handled through IntelManager
	- Removed concept of "comm channels"; channels.json no longer loaded
- Added CombatEntityAPI.getAI() method to get at proximity fuse AI from projectiles
- Added ProximityFuseAIAPI, with an updateDamage() method, so changing damage via script can work on flak and similar
- Added SettingsAPI.getMergedJSONForMod(String path, String masterMod)

## Bugfixing:

- Fixed issue with market/planet name mismatch for some procgen planets
- Fixed issues w/ small dormant remnant fleets not engaging and sometimes dropping loot if harassed
- Ship AI: fixed issue where 4+ carriers could get stuck in an escorting-each-other loop
- Fixed bug that caused Fleet Logistics 1 to make destroyed ships only have a 50% chance of recovery rather than 100% as intended. Disabled ships still got the 100% chance.
- Fixed issue with Plasma Cannon not being properly affected by Safety Overrides
- Save bar no longer get stuck if saving the game throws an error for any reason
- Fixed issue with scavenger fleets stuck in "returning to <market>" state
- Fixed issue that was sometimes preventing carriers from properly obeying "Full Retreat" order
- Fixed rendering issue with black holes on radar
- Fixed issue with Strike Commander causing projectile weapons from fighters to hit multiple times
- Fixed crash when a mission variant had a fighter wing from a mod installed and the mod was then disabled
- Fixed visual issue with jump-point in Westernesse star system
- Fixed issue with debris fields not despawning properly and causing savegame bloat (thanks, Tartiflette!)
- Fixed issue where could select civilian/unarmed ships for pursuit autoresolve
- Fixed issue where fighter chips would disappear from inventory when opening refit screen for a ship with that fighter built in
- Fixed issue with fighters returning to a retreated carrier getting stuck off-map without retreating
- Fixed station rendering issue in campaign where module placement wasn't quite right
- Fixed bug that caused phasing mid-burst to improperly shorten some weapon cooldowns
- The resolutionOverride config file setting now works with fullscreen mode
- Fixed issue that cause a black hole to appear in the middle of the sector map in some games
- Fixed bug that caused the player to have to wait for their allies to finish the battle after full-retreating from it
- Fixed issue that caused REDACTED fleets to respawn extremely quickly in some situations
